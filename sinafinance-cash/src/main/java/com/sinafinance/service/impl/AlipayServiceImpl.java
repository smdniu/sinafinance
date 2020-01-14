package com.sinafinance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.sinafinance.exception.ErrorCode;
import com.sinafinance.exception.SinafinanceException;
import com.sinafinance.service.AlipayService;
import com.sinafinance.utils.StringHelper;
import com.sinafinance.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

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
        model.setPayeeAccount("2088102180339032");
        //测试金额必须大于等于0.1，只支持2位小数，小数点前最大支持13位
        model.setAmount(amount);
        //当付款方为企业账户且转账金额达到（大于等于）50000元，remark不能为空。
        model.setRemark("企业账户提现到支付账户转账备注");
        request.setBizModel(model);
        try {
            AlipayFundTransToaccountTransferResponse transferResponse = alipayClient.execute(request);
            return transferResponse;
        } catch (AlipayApiException e) {
            logger.error(e.getMessage());
            throw new SinafinanceException(ErrorCode.ALIPAY_ERROR.getCode(),ErrorCode.ALIPAY_ERROR.getDesc());
        }
    }

    /**
     * 查询转账结果
     * @return
     */
    public BaseResponse query(String out_biz_no,String order_id){
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

//    public void notifyLogic(String xml) {
//
//        try {
//            //1.对xml进行解析 map
//            Map<String, String> map = WXPayUtil.xmlToMap(xml);
//            //2.验证签名
//            boolean signatureValid = WXPayUtil.isSignatureValid(map, config.getKey());
//
//            System.out.println("验证签名是否正确："+signatureValid);
//            System.out.println(map.get("out_trade_no"));
//            System.out.println(map.get("result_code"));
//
//            //3.修改订单状态
//            if(signatureValid){
//                if("SUCCESS".equals(map.get("result_code"))){
//                    orderService.updatePayStatus( map.get("out_trade_no"),map.get("transaction_id") );
//                    //发送消息给mq
//                    rabbitTemplate.convertAndSend("paynotify","",map.get("out_trade_no"));
//                }else{
//                    //记录日志
//                }
//            }else{
//                //记录日志
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 转账单据状态变更后触发的通知
     * @return
     */
    public BaseResponse changed(String out_biz_no,String order_id){
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
}
