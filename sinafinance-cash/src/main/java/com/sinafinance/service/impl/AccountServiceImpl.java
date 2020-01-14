package com.sinafinance.service.impl;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.sinafinance.pojo.Account;
import com.sinafinance.dao.mapper.AccountMapper;
import com.sinafinance.enums.AccountStateEnum;
import com.sinafinance.exception.ErrorCode;
import com.sinafinance.exception.SinafinanceException;
import com.sinafinance.service.AccountService;
import com.sinafinance.service.AlipayService;
import com.sinafinance.service.DealService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CashOutRequest;
import com.sinafinance.vo.DealVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private DealService dealService;

    @Autowired
    private AlipayService alipayService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse cashOut(CashOutRequest request) {
        /**
         * 支付密码校验
         * 1.根据用户名，支付密码查询当前账户，判断支付密码是否正确
         */
        Example example = new Example(Account.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid",request.getUid());
        criteria.andEqualTo("payPassword",request.getPayPassword());
        List<Account> accounts = accountMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(accounts)){
            logger.info("支付密码错误，请重新输入");
            return BaseResponse.newFailResponse(ErrorCode.PAY_PASSWD_WRONG);
        }

        logger.info("发起提现申请");
        //创建交易,返回交易id
        String dealId = "";
        try {
            dealId = dealService.addDeal(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SinafinanceException("创建交易失败");
        }

        //调用第三方支付
        AlipayFundTransToaccountTransferResponse alipayResponse = null;
        try {
            alipayResponse = alipayService.transToaccount(request.getBandAccountType(), dealId, request.getMoney().toString(), "提现到支付宝账户");
            /**
             * {"alipay_fund_trans_toaccount_transfer_response":
             * {"code":"10000",
             * "msg":"Success",
             * "order_id":"20200109110070001502220000436462",
             * "out_biz_no":"2018101149542322343211111",
             * "pay_date":"2020-01-09 16:21:53"},
             * "sign":"OctnK1+fTwvF0jGSWE/y28oljBgUxJdPsoYO3fgHKRRPGT9jFZLe6DgnsbpBY8a60bRL7xLP/GqmorxtaAqtrFskb2fJIVIyi2BZoWjtzUTnkpyHDVXK9M2q4imeQJwZ61LaM+4vZ2yzPKIOaS0pNSo3WlkeDQKMPE8F4MIcqsEVkNwlAcPOx02FuKGnI/2hBQWTFUIrurPRJvy1KmJE/GPoEMXJXT2/LK2qXLHpLlIkf4F0l+spBvv2hNEghsphag3FwXQtkhlwR4UJNeIrqtIJQEzpMhr8HnibMyl/JN5AGei4ESCw0o3f2aTcTuOw0+DRG4o/wCkfYZPYOr8qHA=="}
             */
            if (StringUtils.isEmpty(alipayResponse)) {
                logger.error("接口调用失败,返回值为空");
                throw new SinafinanceException(ErrorCode.ALIPAY_RESP_NULL.getCode(),ErrorCode.ALIPAY_RESP_NULL.getDesc());
            }
            if ("10000" != alipayResponse.getCode()) {
                logger.error("接口调用失败,状态码不正确:" + alipayResponse.toString());
                throw new SinafinanceException(ErrorCode.ALIPAY_RESP_CODE_ERROR.getCode(),ErrorCode.ALIPAY_RESP_CODE_ERROR.getDesc());
            }
            logger.info("支付结果 [{}]",alipayResponse.getBody());
            if(alipayResponse.isSuccess()){
                logger.info("支付成功");
                DealVO dealVO = dealService.findById(alipayResponse.getOutBizNo());
                dealVO.setOrder_id(alipayResponse.getOrderId());
                dealVO.setDealState("2");
                return BaseResponse.newSuccResponse(alipayResponse);
            } else {
                logger.error("支付失败:" + alipayResponse.getMsg());
                throw new SinafinanceException(ErrorCode.ALIPAY_RESP_MSG_ERROR.getCode(),ErrorCode.ALIPAY_RESP_MSG_ERROR.getDesc());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SinafinanceException(ErrorCode.ALIPAY_ERROR.getCode(),ErrorCode.ALIPAY_ERROR.getDesc());
        }
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Account findById(String id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public boolean reduceAccount(String uid,BigDecimal payMoney) {

        //检查是否可以扣减账户余额
        boolean idDeduction=true;//是否可以扣减
        //判断账户状态
        Account account = findById(uid);
        if(account==null){
            idDeduction=false;
        }
        if(!AccountStateEnum.NORMAL.equals(account.getState())){
            idDeduction=false;
        }
        if( account.getMoney().compareTo(payMoney) < 0 ){
            idDeduction=false;
        }

        //执行扣减
        if(idDeduction){
            accountMapper.reduceMoney(uid,payMoney);//扣减库存
        }
        return idDeduction;
    }




}
