package cn.mrsunflower.sinafinancecashfei.service.impl;

import cn.mrsunflower.sinafinancecashfei.mapper.*;
import cn.mrsunflower.sinafinancecashfei.pojo.*;
import cn.mrsunflower.sinafinancecashfei.service.WithdrawalService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sinafinance.enums.ResponseCode;
import com.sinafinance.enums.WithdrawalConstant;
import com.sinafinance.exception.ErrorCode;
import com.sinafinance.service.AlipayService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CashOutRequest;
import com.sinafinance.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("all")
@Transactional
@Service
public class WithdrawalServiceImpl implements WithdrawalService {
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
    public BaseResponse cashOut(CashOutRequest request) {
        /**
         * 第一步:判断支付密码是否输入正确
         */
        Example example = new Example(PayPassword.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid", request.getUid());
        criteria.andEqualTo("password", request.getPayPassword());
        List<PayPassword> payPasswords = payPasswordMapper.selectByExample(example);
        if (payPasswords.size() == 0) {
            return new BaseResponse().newFailResponse(ErrorCode.SYSTEM_ERROR.getCode(), "支付密码错误");
        }

        /**
         * 第二步,判断有没有绑定帐户
         */
        Example example1 = new Example(WithdrawalBank.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("bankNumber", request.getBandAccountId());
        List<WithdrawalBank> withdrawalBanks = withdrawalBankMapper.selectByExample(example1);
        if (withdrawalBanks.size() == 0) {
            return new BaseResponse().newFailResponse(ErrorCode.SYSTEM_ERROR.getCode(), "没有绑定银行卡");
        }

        /**第三步:得到可提现金额
         * 可提现金额=帐户金额-手续费-转账金额(红包金额)+...
         * ps:这里可以使用上面的uid查询可转账余额
         */
        System.out.println("------查询用户可提现金额-------");
        //这里没有实现,假如这里只有2000000块可以提现的了
        BigDecimal billMoney = new BigDecimal(2000000L);

        /**
         * 第四步:判断可提现金额是否小于申请提现金额
         */
        //logger.info("[WithdrawalController][applyWithDrawal]当前用户userId:" + userId + " 可提现金额:" + billMoney);
        //拿到申请提现的金额
        BigDecimal withdrawApplyTotal = request.getMoney();
        if (withdrawApplyTotal.compareTo(billMoney) == 1) {
            return new BaseResponse().newFailResponse(ResponseCode.ERROR.getCode(), "申请金额错误,返回重试!");
        }
        /**
         * 第五步,插入申请提现信息
         */
        //创建提现详情类
        WithdrawalInfo withdrawal = new WithdrawalInfo();
        withdrawal.setUid(Long.parseLong(request.getUid()));
        withdrawal.setId(System.currentTimeMillis());
        //生成提现订单号,可以使用使用推特的雪花算法生成,我这里随便弄的一个UUID
        withdrawal.setWithdrawApplyTime(new Timestamp(System.currentTimeMillis()));
        String orderNumber = UUID.randomUUID().toString();
        withdrawal.setWithdrawOrder(orderNumber);
        withdrawal.setWithdrawBankId(UUID.randomUUID().toString());
        //添加创建时间
        withdrawal.setCreateTime(new Timestamp(System.currentTimeMillis()));
        // 申请提现.修改提现订单状态
        withdrawal.setStatus(WithdrawalConstant.APPLY_WITHDRAWAL.getStatus());

        withdrawalMapper.insert(withdrawal);

        /**
         * 第六步:插入提现申请日志
         */
        //提现日志
        WithdrawalLogs withdrawalLogs = new WithdrawalLogs();
        withdrawalLogs.setId(System.currentTimeMillis());
        withdrawalLogs.setWithdrawOrder(orderNumber);
        withdrawalLogs.setStatus(WithdrawalConstant.APPLY_WITHDRAWAL.getStatus());
        withdrawalLogs.setOperater(request.getUid());
        withdrawalLogs.setRemark("提现已提交,审核中!");
        //记录提现日志和提现order
        withdrawalLogsMapper.insert(withdrawalLogs);

        /**
         * 第七步:在用户帐户中减去提现的金额
         */
        BaseResponse baseResponse1 = updataAccount(Long.parseLong(request.getUid()), request.getMoney(), 0);
        if (ErrorCode.SYSTEM_ERROR.getCode().equals(baseResponse1.getRespCode())) {
            return baseResponse1;
        }

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setRespMsg("提现申请成功");
        baseResponse.setRespCode("20000");
        return baseResponse;

    }

    /**
     * 用于修改金额
     *
     * @param uid   用户id
     * @param money 增加或者减少的金额
     * @param type  0,减少,1增加
     */
    public BaseResponse updataAccount(Long uid, BigDecimal money, int type) {
        //查询余额
        Example selectExample = new Example(Account.class);
        Example.Criteria selectCriteria = selectExample.createCriteria();
        selectCriteria.andEqualTo("uid", uid);
        List<Account> accounts = accountMapper.selectByExample(selectExample);
        if (accounts.size() == 0) {
            //表示没有查询到该帐户
            return BaseResponse.newFailResponse(ErrorCode.SYSTEM_ERROR);
        }
        //余额
        Account account = accounts.get(0);

        if (type == 1) {
            account.setAccount(account.getAccount().add(money));
        } else {
            //减去余额
            account.setAccount(account.getAccount().subtract(money));
        }
        accountMapper.updateByExample(account, selectExample);
        return new BaseResponse().newSuccResponse(null);
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
    public BaseResponse check(Long userId, Long cashId, int type) {
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", cashId);
        criteria.andEqualTo("status", WithdrawalConstant.APPLY_WITHDRAWAL.getStatus());
        List<WithdrawalInfo> withdrawalInfos = withdrawalMapper.selectByExample(example);
        if (withdrawalInfos.size() == 0) {
            return new BaseResponse().newFailResponse(ErrorCode.SYSTEM_ERROR);
        }
        WithdrawalInfo withdrawalInfo = withdrawalInfos.get(0);
        //type 0 未通过审核,1 通过审核
        if (type == 0) {
            //查询订单

            //修改余额
            BigDecimal withdrawApplyTotal = withdrawalInfo.getWithdrawApplyTotal();
            updataAccount(userId, withdrawApplyTotal, 1);

            //修改提现状态
            updateWithdrawalStatus(cashId, WithdrawalConstant.FAILURE.getStatus());

        }
        if (type == 1) {

            System.out.println("--------假如发起了转账--------");
            //调用支付宝转账
            //调用第三方支付
//            AlipayFundTransToaccountTransferResponse alipayResponse = null;
//            try {
//                alipayResponse = alipayService.transToaccount(withdrawalInfo.getWithdrawBankId(), withdrawalInfo.getId()+"", withdrawalInfo.getWithdrawApplyTotal().toString(), "提现到支付宝账户");
//                if (StringUtils.isEmpty(alipayResponse)) {
//                    // logger.error("接口调用失败,返回值为空");
//                    //TODO 这里是否需要判断是否转账成功，如果转账成功，
//                    // 但是因为网络原因返回了错误的结果，那么需要检查是否转账成功，如果转账成功，
//                    // 则本地扣减，如果没有转账成功则回滚操作
//
//
//                    throw new SinafinanceException(ErrorCode.ALIPAY_RESP_NULL.getCode(), ErrorCode.ALIPAY_RESP_NULL.getDesc());
//                }
//                if ("10000" != alipayResponse.getCode()) {
//                    //logger.error("接口调用失败,状态码不正确:" + alipayResponse.toString());
//                    throw new SinafinanceException(ErrorCode.ALIPAY_RESP_CODE_ERROR.getCode(), ErrorCode.ALIPAY_RESP_CODE_ERROR.getDesc());
//                }
//                //logger.info("提现接口调用结果 [{}]",alipayResponse.getBody());
//                if (alipayResponse.isSuccess()) {
//                    //修改交易流水提现状态为处理中
//                    updateWithdrawalStatus(cashId, WithdrawalConstant.PROCESSING.getStatus());
//                    //logger.info("提现处理中,预计到账时间...");
//                    WithdrawalInfo withdrawalInfo1 = withdrawalMapper.selectByPrimaryKey(alipayResponse.getOutBizNo());
//                    if (withdrawalInfo1 != null) {
//                        //修改交易流水状态等信息
//                        return BaseResponse.newSuccResponse(withdrawalInfo1);
//                    } else {
//                        //logger.error("提现失败,查询不到该交易流水");
//                        //throw new SinafinanceException(ErrorCode.DEAL_NULL.getCode(), ErrorCode.DEAL_NULL.getDesc());
//                        throw new SinafinanceException();
//
//                    }
//
//                } else {
//                    //logger.error("提现失败:" + alipayResponse.getMsg());
//                    //提现失败，修改交易流水状态
//                    //记录日志
//                    //账户回退
//                    throw new SinafinanceException(ErrorCode.ALIPAY_RESP_MSG_ERROR.getCode(), ErrorCode.ALIPAY_RESP_MSG_ERROR.getDesc());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                //提现失败，修改交易流水状态
//                //记录日志
//                //账户回退
//                throw new SinafinanceException(ErrorCode.ALIPAY_ERROR.getCode(), ErrorCode.ALIPAY_ERROR.getDesc());
//            }
        }

        return new BaseResponse().newSuccResponse(null);
    }


    public void updateWithdrawalStatus(Long cashId, Long status) {
        Example example = new Example(WithdrawalInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", cashId);

        WithdrawalInfo withdrawalInfo = withdrawalMapper.selectByPrimaryKey(cashId);
        withdrawalInfo.setStatus(status);

        withdrawalMapper.updateByExample(withdrawalInfo, example);
    }

}
