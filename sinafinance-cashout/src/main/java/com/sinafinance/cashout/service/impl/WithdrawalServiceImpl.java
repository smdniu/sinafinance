package com.sinafinance.cashout.service.impl;

import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sinafinance.cashout.mapper.*;
import com.sinafinance.cashout.pojo.*;
import com.sinafinance.cashout.service.AlipayService;
import com.sinafinance.cashout.service.WithdrawalService;
import com.sinafinance.enums.AccountStateEnum;
import com.sinafinance.enums.WithdrawalConstant;
import com.sinafinance.enums.WithdrawalStateEnum;
import com.sinafinance.exception.ErrorCode;
import com.sinafinance.exception.SinafinanceException;
import com.sinafinance.exception.WithdrawalFailureException;
import com.sinafinance.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.sinafinance.cashout.util.WithdrawalLogsUtils.creatWithdrawalLogs;

@SuppressWarnings("all")
@Transactional(rollbackFor = SinafinanceException.class)
@Service("withdrawalService")
public class WithdrawalServiceImpl implements WithdrawalService {
    private Logger logger = LoggerFactory.getLogger(WithdrawalServiceImpl.class);
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private WithdrawalMapper withdrawalMapper;
    @Autowired
    private WithdrawalLogsMapper withdrawalLogsMapper;
    @Autowired
    private PayPasswordMapper payPasswordMapper;
    @Autowired
    private WithdrawalBankMapper withdrawalBankMapper;
    @Autowired
    private AccountMapper accountMapper;


    //根据userid查询提现信息
    public List<WithdrawalInfo> getWithdrawalByUserId(String userId) {
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid", userId);
        List<WithdrawalInfo> withdrawals = withdrawalMapper.selectByExample(example);
        return withdrawals;
    }

    /**
     * 插入提现申请和日志
     *
     * @return
     */
    @Transactional(rollbackFor = SinafinanceException.class,isolation = Isolation.REPEATABLE_READ)
    public BaseResponse cashOut(CashOutRequest request) {
        /**
         * 第一步:判断支付密码是否输入正确
         */
        try {
            Example example = new Example(PayPassword.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("uid", request.getUid());
            criteria.andEqualTo("password", request.getPayPassword());
            List<PayPassword> payPasswords = payPasswordMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(payPasswords) || payPasswords.size() == 0) {
                logger.info("支付密码错误，请重新输入.");
                return new BaseResponse().newFailResponse(ErrorCode.PAY_PASSWD_WRONG);
            }

            /**
             * 第二步,判断有没有绑定帐户
             */
            Example example1 = new Example(WithdrawalBank.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("bankNumber", request.getWithdrawBankId());
            List<WithdrawalBank> withdrawalBanks = withdrawalBankMapper.selectByExample(example1);
            if (CollectionUtils.isEmpty(withdrawalBanks) || withdrawalBanks.size() == 0) {
                logger.info("绑定银行卡[{}]不存在，请重新选择提现卡号.",request.getWithdrawBankId());
                return new BaseResponse().newFailResponse(ErrorCode.BAND_BANKNO_NOT_EXIT);
            }

            logger.info("###########发起提现申请#############");

            /**
             * 第三步,创建交易流水
             */
            WithdrawalInfo withdrawal = new WithdrawalInfo();
            withdrawal.setId(System.currentTimeMillis());
            withdrawal.setWithdrawApplyTotal(request.getWithdrawApplyTotal());
            withdrawal.setWithdrawCharge(request.getWithdrawCharge());
            withdrawal.setWithdrawRealityTotal(request.getWithdrawApplyTotal().add(request.getWithdrawCharge()));
            withdrawal.setUid(request.getUid());
//        //提现订单号,由第三方转账接口返回
//        String orderNumber = UUID.randomUUID().toString();
//        withdrawal.setWithdrawOrder(orderNumber);
            withdrawal.setWithdrawBankId(request.getWithdrawBankId());
            // 申请提现.修改提现订单状态
            withdrawal.setStatus(WithdrawalStateEnum.APPLY_WITHDRAWAL.getCode());//交易状态 1：发起提现 2：处理中 3：到账 4：提现失败 5：退回成功
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR,2);
            withdrawal.setCreateTime(date);
            withdrawal.setLastUpdateTime(date);
            withdrawal.setExpectTime(calendar.getTime());//预计到账时间
            withdrawal.setWithdrawApplyTime(new Timestamp(System.currentTimeMillis()));
            withdrawalMapper.insert(withdrawal);

            /**
             * 第四步:插入提现申请日志
             */
            //创建提现日志
            WithdrawalLogs withdrawalLogs = creatWithdrawalLogs(withdrawal, request);

            //记录提现日志和提现order
            withdrawalLogsMapper.insert(withdrawalLogs);

            /**
             * 第五步:在用户帐户中减去提现的金额
             */
            BigDecimal totalAccount = request.getWithdrawApplyTotal().add(request.getWithdrawCharge());
            if (! updataAccount(request.getUid(), totalAccount, 0)){
                logger.info("扣减本地账户余额出错，请稍后重试！");
                throw new SinafinanceException("扣减本地账户余额出错，请稍后重试！");
            }

//            BaseResponse baseResponse = new BaseResponse();
//            baseResponse.setRespMsg("提现申请成功");
//            baseResponse.setRespCode("00");
            return BaseResponse.newSuccResponse(withdrawal);
        }  catch (SinafinanceException e) {
            logger.info("提现申请失败，请稍后重试！");
            throw new SinafinanceException("扣减本地账户余额出错，请稍后重试！");
        }
    }


    /**
     * 用于修改金额
     *
     * @param uid   用户id
     * @param money 增加或者减少的金额
     * @param type  0,减少,1增加
     */
    public Boolean updataAccount(String uid, BigDecimal money, int type) {
        //检查是否可以扣减账户余额
        boolean idDeduction=true;//是否可以扣减
        //判断账户状态
        //查询余额
        Example selectExample = new Example(Account.class);
        Example.Criteria selectCriteria = selectExample.createCriteria();
        selectCriteria.andEqualTo("uid", uid);
        List<Account> accounts = accountMapper.selectByExample(selectExample);
        if (!CollectionUtils.isEmpty(accounts) && accounts.size()>0) {
            Account account = accounts.get(0);
            if (!AccountStateEnum.NORMAL.getCode().equals(account.getState())) {
                idDeduction = false;
            }
            if (type == 0 && account.getAccount().compareTo(money) < 0) {
                idDeduction = false;
            }
            //执行扣减
            if (idDeduction) {
                if (type == 1) {
                    account.setAccount(account.getAccount().add(money));
                } else {
                    //减去余额
                    account.setAccount(account.getAccount().subtract(money));
                }
                accountMapper.updateByExample(account, selectExample);
            }
        }else {
            idDeduction = false;
        }
        return idDeduction;

    }


    /**
     * 分页查询提现信息
     */
    public PageResult applyWithDrawalList(Long userId, int currentPageNum, int currentPageSize) {
        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(currentPageNum, Math.min(currentPageSize, 100));
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid", userId);
        List<WithdrawalInfo> withdrawals = withdrawalMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(withdrawals);

        PageResult pageResult = new PageResult();
        pageResult.setRows(withdrawals);
        pageResult.setTotal(pageInfo.getTotal());


        return pageResult;
    }

    //根据订单号查询提现信息
    public WithdrawalInfo getWithdrawalByWithdrawalOrderAndStatus(String withdrawalOrder, String status) {
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("withdraw_order", withdrawalOrder);
        criteria.andEqualTo("status", status);
        example.orderBy("createime");
        List<WithdrawalInfo> withdrawals = withdrawalMapper.selectByExample(example);
        if (withdrawals.size() == 0) {
            return null;
        }
        return withdrawals.get(0);
    }

    @Override
    public BaseResponse check(CheckWithdrawRequest request) {
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", request.getWithdrawalId());
        criteria.andEqualTo("status", WithdrawalStateEnum.APPLY_WITHDRAWAL.getCode());
        List<WithdrawalInfo> withdrawalInfos = withdrawalMapper.selectByExample(example);
        if (withdrawalInfos.size() == 0) {
            return BaseResponse.newFailResponse(ErrorCode.WITHDRAWALI_NOT_EXIT.getCode(),ErrorCode.WITHDRAWALI_NOT_EXIT.getDesc());
        }
        WithdrawalInfo withdrawalInfo = withdrawalInfos.get(0);
        //type 0 未通过审核,1 通过审核
        if ("0".equals(request.getType())) {
            //修改余额
            BigDecimal withdrawApplyTotal = withdrawalInfo.getWithdrawRealityTotal();
            updataAccount(request.getUserId(), withdrawApplyTotal, 1);
            //修改提现状态，记录日志
            updateWithdrawalStatus(request.getWithdrawalId(),null, WithdrawalStateEnum.FAILURE.getCode());
        }
        if ("1".equals(request.getType())) {

            System.out.println("--------假如发起了转账--------");
            //调用支付宝转账
            //调用第三方支付
            AlipayFundTransToaccountTransferResponse alipayResponse = null;
            try {
                String accountId = "2088102180339032";//沙箱测试转账方 支付宝账户商家UID 2088102180339032
//                alipayResponse = alipayService.transToaccount(withdrawalInfo.getWithdrawBankId(),withdrawalInfo.getId()+"", withdrawalInfo.getWithdrawApplyTotal().toString(), "提现到支付宝账户");
                alipayResponse = alipayService.transToaccount(withdrawalInfo.getWithdrawBankId(),withdrawalInfo.getId()+"", withdrawalInfo.getWithdrawApplyTotal().toString(), "提现到支付宝账户");
                /**
                 * {"alipay_fund_trans_toaccount_transfer_response":
                 * {"code":"10000",
                 * "msg":"Success",
                 * "order_id":"20200109110070001502220000436462",
                 * "out_biz_no":"2018101149542322343211111",
                 * "pay_date":"2020-01-09 16:21:53"},
                 * "sign":"OctnK1+fTwvF0jGSWE/y28oljBgUxJdPsoYO3fgHKRRPGT9jFZLe6DgnsbpBY8a60bRL7xLP/GqmorxtaAqtrFskb2fJIVIyi2BZoWjtzUTnkpyHDVXK9M2q4imeQJwZ61LaM+4vZ2yzPKIOaS0pNSo3WlkeDQKMPE8F4MIcqsEVkNwlAcPOx02FuKGnI/2hBQWTFUIrurPRJvy1KmJE/GPoEMXJXT2/LK2qXLHpLlIkf4F0l+spBvv2hNEghsphag3FwXQtkhlwR4UJNeIrqtIJQEzpMhr8HnibMyl/JN5AGei4ESCw0o3f2aTcTuOw0+DRG4o/wCkfYZPYOr8qHA=="}
                 */
//                alipayResponse =
//                        FastJsonUtils.fromJsonFile("macroEchatsRequest.json", AlipayFundTransToaccountTransferResponse.class);


                if (StringUtils.isEmpty(alipayResponse)) {
                    logger.error("接口调用失败,返回值为空");
                    //修改余额
                    BigDecimal withdrawApplyTotal = withdrawalInfo.getWithdrawRealityTotal();
                    updataAccount(request.getUserId(), withdrawApplyTotal, 1);
                    //修改提现状态，记录日志
                    updateWithdrawalStatus(request.getWithdrawalId(),null, WithdrawalStateEnum.FAILURE.getCode());
                    return BaseResponse.newFailResponse(ErrorCode.ALIPAY_RESP_NULL.getCode(),ErrorCode.ALIPAY_RESP_NULL.getDesc());
                }
                System.out.println(alipayResponse.getCode());
                if (! "10000".equals(alipayResponse.getCode())) {
                    logger.error("接口调用失败,状态码不正确:" + alipayResponse.toString());
                    //修改余额
                    BigDecimal withdrawApplyTotal = withdrawalInfo.getWithdrawRealityTotal();
                    updataAccount(request.getUserId(), withdrawApplyTotal, 1);
                    //修改提现状态，记录日志
                    updateWithdrawalStatus(request.getWithdrawalId(),null,WithdrawalStateEnum.FAILURE.getCode());
                    return BaseResponse.newFailResponse(ErrorCode.ALIPAY_RESP_CODE_ERROR.getCode(),ErrorCode.ALIPAY_RESP_CODE_ERROR.getDesc());
                }
                logger.info("第三方提现申请结果 [{}]",alipayResponse.getBody());
                if(alipayResponse.isSuccess()){
                    logger.info("第三方提现申请提交成功");
                    withdrawalInfo.setWithdrawOrder(alipayResponse.getOrderId());
                    withdrawalInfo.setStatus(WithdrawalStateEnum.PROCESSING.getCode());//2 处理中
                    updateWithdrawalStatus(request.getWithdrawalId(),alipayResponse.getOrderId(), WithdrawalConstant.PROCESSING.getStatus());

                    //启动一个线程异步回调第三方提现结果

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NotifyRequest req = new NotifyRequest();
                            req.setUserId(withdrawalInfo.getUid());
                            req.setWithdrawalId(withdrawalInfo.getId());
                            req.setWithdrawOrder(withdrawalInfo.getWithdrawOrder());
                            notifyLogic(req);
                        }
                    }).start();

                    return BaseResponse.newSuccResponse(withdrawalInfo);
                } else {
                    logger.error("支付失败:" + alipayResponse.getMsg());
                    //修改余额
                    BigDecimal withdrawApplyTotal = withdrawalInfo.getWithdrawRealityTotal();
                    updataAccount(request.getUserId(), withdrawApplyTotal, 1);
                    //修改提现状态，记录日志
                    updateWithdrawalStatus(request.getWithdrawalId(),null, WithdrawalStateEnum.FAILURE.getCode());
                    return BaseResponse.newFailResponse(ErrorCode.ALIPAY_RESP_MSG_ERROR.getCode(),ErrorCode.ALIPAY_RESP_MSG_ERROR.getDesc());
                }
            } catch (Exception e) {
                //修改余额
                BigDecimal withdrawApplyTotal = withdrawalInfo.getWithdrawRealityTotal();
                updataAccount(request.getUserId(), withdrawApplyTotal, 1);
                //修改提现状态，记录日志
                updateWithdrawalStatus(request.getWithdrawalId(),null, WithdrawalStateEnum.FAILURE.getCode());
                e.printStackTrace();
                throw new WithdrawalFailureException(ErrorCode.ALIPAY_ERROR.getCode(),ErrorCode.ALIPAY_ERROR.getDesc());
            }
        }

        return new BaseResponse().newSuccResponse(null);
    }

    @Override
    public BaseResponse notifyLogic(NotifyRequest request) {
        System.out.println("支付回调请求。。。。");
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", request.getWithdrawalId());
        criteria.andEqualTo("status", WithdrawalStateEnum.PROCESSING.getCode());
        List<WithdrawalInfo> withdrawalInfos = withdrawalMapper.selectByExample(example);
        if (withdrawalInfos.size() == 0) {
            return BaseResponse.newFailResponse(ErrorCode.WITHDRAWPROCESS_NOT_EXIT.getCode(),ErrorCode.WITHDRAWPROCESS_NOT_EXIT.getDesc());
        }
        WithdrawalInfo withdrawalInfo = withdrawalInfos.get(0);
        try {
//            int i = 1/0;
            //调用第三方回调接口
            //  alipayService.notify();
            logger.info("转账成功。。。。。");
            //修改交易状态，记录日志
            withdrawalInfo.setStatus(WithdrawalStateEnum.SUCCESS.getCode());//3 成功
            updateWithdrawalStatus(request.getWithdrawalId(),request.getWithdrawOrder(), WithdrawalStateEnum.SUCCESS.getCode());
            //mq 推送提现成功

            return BaseResponse.newResponse(ErrorCode.SUCCESS.getCode(),"提现成功",withdrawalInfo);

        } catch (Exception e) {
            Boolean transResult = false;
            logger.info("核查转账结果.......");
//           transResult = alipayService.query();
            if (transResult){
                //转账成功
                logger.info("转账成功。。。。。");
                //修改交易状态，记录日志
                withdrawalInfo.setStatus(WithdrawalStateEnum.SUCCESS.getCode());//3 成功
                updateWithdrawalStatus(request.getWithdrawalId(),request.getWithdrawOrder(), WithdrawalStateEnum.SUCCESS.getCode());
                //mq 推送提现成功
                return BaseResponse.newResponse(ErrorCode.SUCCESS.getCode(),"提现成功",withdrawalInfo);
            }else {
                logger.info("转账失败。。。。。");
                //修改余额
                BigDecimal withdrawApplyTotal = withdrawalInfo.getWithdrawRealityTotal();
                updataAccount(request.getUserId(), withdrawApplyTotal, 1);
                //修改提现状态，记录日志
                updateWithdrawalStatus(request.getWithdrawalId(),request.getWithdrawOrder(), WithdrawalStateEnum.FAILURE.getCode());
                //mq 推送提现失败
                e.printStackTrace();
                throw new WithdrawalFailureException(ErrorCode.NOTIFY_ERROR.getCode(),ErrorCode.NOTIFY_ERROR.getDesc());
            }
        }
    }


    public void updateWithdrawalStatus(Long cashId, String orderNumber,Long status) {
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", cashId);

        WithdrawalInfo withdrawalInfo = withdrawalMapper.selectByPrimaryKey(cashId);
        withdrawalInfo.setStatus(status);
        if (!StringUtils.isEmpty(orderNumber)){
            withdrawalInfo.setWithdrawOrder(orderNumber);
        }
        withdrawalInfo.setLastUpdateTime(new Date());

        withdrawalMapper.updateByExample(withdrawalInfo, example);

        //记录日志
        WithdrawalLogs withdrawalLogs = new WithdrawalLogs();
        withdrawalLogs.setId(System.currentTimeMillis());
        withdrawalLogs.setWithdrawId(cashId);
        if (!StringUtils.isEmpty(orderNumber)){
            withdrawalLogs.setWithdrawOrder(orderNumber);
        }
        withdrawalLogs.setStatus(status);
        withdrawalLogs.setOperater(withdrawalInfo.getUid());
        withdrawalLogs.setRemark(WithdrawalStateEnum.getByCode(status).getDesc());
        withdrawalLogs.setCreateTime(new Date());
        //记录提现日志和提现order
        withdrawalLogsMapper.insert(withdrawalLogs);

    }

}
