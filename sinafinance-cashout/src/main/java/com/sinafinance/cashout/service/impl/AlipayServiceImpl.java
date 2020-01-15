package com.sinafinance.cashout.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.sinafinance.cashout.config.AlipayConfig;
import com.sinafinance.cashout.service.AlipayService;
import com.sinafinance.cashout.service.WithdrawalService;
import com.sinafinance.enums.AlipayTradeStatus;
import com.sinafinance.exception.ErrorCode;
import com.sinafinance.exception.SinafinanceException;
import com.sinafinance.vo.AlipayNotifyParam;
import com.sinafinance.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/9 16:45
 */
@Service("alipayService")
public class AlipayServiceImpl implements AlipayService {
    private static final Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

    @Override
    public AlipayFundTransToaccountTransferResponse transToaccount(String accountId, String outBizNo, String amount,String remark) {
        //设置企业账户信息，从配置文件中读取
        AlipayClient alipayClient =
                new DefaultAlipayClient(
                        "https://openapi.alipaydev.com/gateway.do",
                        "2016101900725931",
                        "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDBVV+4FyFGWeKyVrbeLemGhQvrgc4AMuvUEJQjwmrnlrLrJLl2+wIht4M3v150z/YuUIrIhC+LMdNLuGosUQjVDhuVxaC+UlgC9YQGcy6Z88Wa2tblKQHwF5vzcswi/HRF9VHjnRngdDGE0vLc9Kf3uqlISTWPKvzRMgWTWwVg7fb6OG2aLfDjWIFLAO885/w97ai01ZegZjlUUyTxhK9mR7kQO0Ag3ye4Rvls3feQkXNwiRfIwB5rNRGGNFqqNXPLZUnnmrEr+ZnxSN/qh2e0tcu25Ct6MUIboSBj1Bh+vCKrcJmk169LB355soHtbfq2rHLMk6LdIXZEfJGXiNV5AgMBAAECggEAUZyJcHuLF1iAQsoqHbYB20A0nPDJlfno5TcuYb4DRMmvs2VoSRqCdtsxsAm/hM343Oow+54ZN8gLqu4pG5YFhXKUIHAxf8Xg0zbyyYo7xM9LvifdCRV7dO9ybJ0sISSyrIl3TP8XWlGSScCGttEsSn3oUdRdoJcD7AcSpXaJ2bU+D5xcO+riiP8T1iZ8OfLY5SSpTcHOQkXZl9m+oPVN22tVKVDghL1AWfTlgJpm4RLq/QMTzybxpBapnur8fR2240MoaMBDWaklMYYA6bXPCW41GEeMZL919rVxn5J657HTbML1xFuKDBd2evWI2dRuPcoOQ2ZQdGYDpW2OK0ZSUQKBgQDtgL+jRU/T2BLGyOFEOfcsgomb1LAc3tEzrtVS6tGjbZsJpV645tJJzKbVqfP5q8aK/VBqMpNIX8aHdqc76WqCIzHrafMDm5EstvIwyJBlHo2bp5hugsYfXNL3gQ872xSYgymhLaxCYN+ZfKqWOhSpKHMVJbtz5WwiuiCTxgLizQKBgQDQY/2LIuWyeyRx69MCCYwKoYy5j9MMbzSxkkAMZCR/SrvDXxJw6SOhO8vSUOuR1dv2NfZQKRPtazsvRyG/SHd6C60Vl5Fpt7ZmCnox/XPvhaC6p7uzOp6IYC8M/tMp2XUKtjR1YsDgSEdG/TKGoTWUpFP/QthVNxwDRVqGHag1XQKBgBWUpFUuoCFMufAMIsPzTG1j8yMQwWvuJy4+Da46HhBuji7jMuc0OhlwmhxCFCQ0opbQi7UKoW1rHnwCoRGFlSL8FbYha26BfUCF4x3hq5U0oINrSCOKiHwjEBxjQsIalxWAnZDCuKtTaB3nQQzjfCsevM5IHm9agmfpZ7plvK9lAoGAPszDTbjYi+78TrCIyGZkI5gHtqwXJ/ojYRf34UQUk5/L8dam0gwQRrikdd8Y/rv6K7qbXhDjBnt4PIktLF4EU21NXeJtWfSZLSsfJA2DcL1OcLG3qQ2TVPnXdA0fSc9UrEO4j1+RTIW7pTMMlPHkGcVj4tscIqIW+Q1feGdbl6kCgYARGaEjgkaftAY6UyF88e88zHc+ugThj+m6oRG2e6AKThpFI78SEvpio6029shJdEhx9vUCjr+uZ7svkgcjhOGZRfttbFKb5T3syShY2UDrkpaF7bYlp+pfsiaUrucr0iqtOx/nmMRE9Zv2CIMPwxdQoDWWN4ZwEEUSMvdbMN4YvQ==",
                        "json", "utf-8",
                        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgkx0fC4/R/yFNQ49I02dTCSg8xwm5t/HKPjCJ+S93CSaOxDT12QHhYuLqtpcEkOoh0bAy1Cyu4O1t7/QV9m9PVMKtyqnP5vVE8OUip2kYWwbaW47/FxA0nkTvS20Gzwvi7k2Oj87bVrL9VLTogYn9UvZDoHMlpwsCn8JkISrKA5RSooqwKMCeePjfknZfTtvN6MkXN3uMp1rhRr1XZBw+M7AxkKca0YQfT6JcvL0Moh56cMFebjzqof3oECnbKdD5F2UByVI+mCWzx0BpK5ptqw2bfr0ldD4Kx3ITYz8eRVdSy4pFzGQBZkA3dVHq64Ynl42l1RwA0kY1+sGO+U/LQIDAQAB",
                        "RSA2");

        //1.获取绑定的支付宝账户或银行账号,可从表中读取用户绑定账户信息

        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        //商户转账唯一订单号
        model.setOutBizNo(outBizNo);
        //收款方账户类型。
        //1、PayeeType=ALIPAY_USERID：PayeeAccount传值pid ,以2088开头的16位纯数字组成。
        //2、PayeeType=ALIPAY_LOGONID：PayeeAccount传值支付宝登录号(邮箱或手机号)
        model.setPayeeType("ALIPAY_USERID");
        //收款方账户。与payee_type配合使用。付款方和收款方不能是同一个账户。
        //另一个支付宝账户商家UID
//        model.setPayeeAccount("2088102180339032");
        model.setPayeeAccount(accountId);
        //测试金额必须大于等于0.1，只支持2位小数，小数点前最大支持13位
        model.setAmount(amount);
        //当付款方为企业账户且转账金额达到（大于等于）50000元，remark不能为空。
//        model.setRemark("企业账户提现到支付账户转账备注");
        model.setRemark(remark);
        request.setBizModel(model);
        try {
            AlipayFundTransToaccountTransferResponse transferResponse = alipayClient.execute(request);
            return transferResponse;
        } catch (AlipayApiException e) {
            logger.error(e.getMessage());
            throw new SinafinanceException(ErrorCode.ALIPAY_ERROR.getCode(), ErrorCode.ALIPAY_ERROR.getDesc());
        }
    }
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    private WithdrawalService withdrawalService;


    @Autowired
    @Qualifier("withdrawalService")
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    /**
     * 查询转账结果
     * @return
     */
    public BaseResponse query(String out_biz_no, String order_id){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        request.setBizContent("{" +
                "\"product_code\":\"STD_RED_PACKET\"," +
                "\"biz_scene\":\"PERSONAL_PAY\"," +
                "\"out_biz_no\":\"201808080001\"," +
                "\"order_id\":\"20190801110070000006380000250621\"," +
                "\"pay_fund_order_id\":\"20190801110070001506380000251556\"" +
                "  }");
        AlipayFundTransCommonQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return BaseResponse.newSuccResponse(response);
    }


    /**
     * 转账单据状态变更后触发的通知
     * @return
     */
    public BaseResponse changed(String out_biz_no, String order_id){
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openasyncapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
//        AlipayFundTransOrderChangedRequest request = new AlipayFundTransOrderChangedRequest();
//        request.setBizContent("{" +
//                "\"out_biz_no\":\"201806300001\"," +
//                "\"product_code\":\"STD_RED_PACKET\"," +
//                "\"biz_scene\":\"PERSONAL_PAY\"," +
//                "\"origin_interface\":\"alipay.fund.trans.app.pay\"," +
//                "\"pay_fund_order_id\":\"20190801110070001506380000251556\"," +
//                "\"order_id\":\"20190624110075000006530000014566\"," +
//                "\"status\":\"SUCCESS\"," +
//                "\"action_type\":\"FINISH\"," +
//                "\"trans_amount\":\"32.00\"," +
//                "\"fail_reason\":\"单笔额度超限\"," +
//                "\"passback_params\":\"{\\\"merchantBizType\\\":\\\"peerPay\\\"}\"," +
//                "\"error_code\":\"BANK_ACCOUNT_ERROR\"," +
//                "\"pay_date\":\"2013-01-01 08:08:08\"," +
//                "\"pay_user_id\":\"例如：2088611907527864\"," +
//                "\"payment_status\":\"FAIL\"," +
//                "\"payment_error_code\":\"PAYEE_USERINFO_STATUS_ERROR\"," +
//                "\"payment_fail_reason\":\"收款方用户状态不正常\"" +
//                "  }");
//        AlipayFundTransOrderChangedResponse response = null;
//        try {
//            response = alipayClient.execute(request);
//            if(response.isSuccess()){
//                System.out.println("调用成功");
//            } else {
//                System.out.println("调用失败");
//            }
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }

        return BaseResponse.newSuccResponse(null);
    }


    /**
     * <pre>
     * 第一步:验证签名,签名通过后进行第二步
     * 第二步:按一下步骤进行验证
     * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
     * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
     * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
     * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
     * </pre>
     *
     * @param
     * @return
     */
    public String callback(HttpServletRequest request) {
        Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String paramsJson = JSON.toJSONString(params);
        logger.info("支付宝回调，{}", paramsJson);
        try {
            AlipayConfig alipayConfig = new AlipayConfig();// 支付宝配置
            // 调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(),
                    alipayConfig.getCharset(), alipayConfig.getSigntype());
            if (signVerified) {
                logger.info("支付宝回调签名认证成功");
                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
                this.check(params);
                // 另起线程处理业务
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        AlipayNotifyParam param = buildAlipayNotifyParam(params);
                        String trade_status = param.getTradeStatus();
                        // 支付成功
                        if (trade_status.equals(AlipayTradeStatus.TRADE_SUCCESS.getStatus())
                                || trade_status.equals(AlipayTradeStatus.TRADE_FINISHED.getStatus())) {
                            // 处理支付成功逻辑
                            try {
                                /*
                                    // 处理业务逻辑。。。
                                   1.修改交易状态为提现成功。
                                   2.跳转到提现成功页面

                                */

//                                Deal deal = dealService.findById(param.getOutBizNo());
//                                if(deal!=null) {
//                                    //修改交易流水状态为提现成功等信息
//                                    deal.setDealState("3");
//                                    deal.setUpdateTime(new Date());//修改日期
//                                    deal.setEndTime(new Date());//到账日期
//                                    dealService.updateDealStatus(deal);
////                                //js订阅该主题，发送消息给mq,js跳转到提现成功页面，展示提现信息,有时间可以实现
////                                rabbitTemplate.convertAndSend("withdrawlnotify","",param.getOutBizNo());
//                                }else {
//                                    logger.error("提现失败,查询不到该交易流水");
//                                }

                            } catch (Exception e) {
                                logger.error("支付宝回调业务处理报错,params:" + paramsJson, e);
                            }
                        } else {
                            logger.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}",trade_status,paramsJson);
                            //提现失败，修改交易流水状态
                            //记录日志
                            //账户回退
                            //发送回滚消息
//            rabbitTemplate.convertAndSend("","queue.cashback", JSON.toJSONString(orderItemList));
                        }
                    }
                });
                // 如果签名验证正确，立即返回success，后续业务另起线程单独处理
                // 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
                return "success";
            } else {
                logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", paramsJson);
                //提现失败，修改交易流水状态
                //记录日志
                //账户回退
                return "failure";
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}", paramsJson, e.getMessage());
            //提现失败，修改交易流水状态
            //记录日志
            //账户回退
            return "failure";
        }
    }

    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }

    private AlipayNotifyParam buildAlipayNotifyParam(Map<String, String> params) {
        String json = JSON.toJSONString(params);
        return JSON.parseObject(json, AlipayNotifyParam.class);
    }

    /**
     * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
     * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
     * 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
     * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
     *
     * @param params
     * @throws AlipayApiException
     */
    private void check(Map<String, String> params) throws AlipayApiException {
        String outTradeNo = params.get("out_trade_no");

        // 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，

//        Order order = getOrderByOutTradeNo(outTradeNo); // 这个方法自己实现
//        if (order == null) {
//            throw new AlipayApiException("out_trade_no错误");
//        }
//
//        // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
//        long total_amount = new BigDecimal(params.get("total_amount")).multiply(new BigDecimal(100)).longValue();
//        if (total_amount != order.getPayPrice().longValue()) {
//            throw new AlipayApiException("error total_amount");
//        }
//
//        // 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
//        // 第三步可根据实际情况省略
//
//        // 4、验证app_id是否为该商户本身。
//        if (!params.get("app_id").equals(alipayConfig.getAppid())) {
//            throw new AlipayApiException("app_id不一致");
//        }
    }
}
