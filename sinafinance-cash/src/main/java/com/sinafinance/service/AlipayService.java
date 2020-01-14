package com.sinafinance.service;

import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.sinafinance.vo.BaseResponse;

public interface AlipayService {

    AlipayFundTransToaccountTransferResponse transToaccount(String accountId, String outBizNo, String amount, String remark);
    BaseResponse query(String out_biz_no, String order_id);
    BaseResponse changed(String out_biz_no,String order_id);
}
