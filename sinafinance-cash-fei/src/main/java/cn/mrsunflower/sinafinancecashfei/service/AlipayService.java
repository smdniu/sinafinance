package com.sinafinance.service;

import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.sinafinance.vo.BaseResponse;

public interface AlipayService {
    /**
     *转账到
     * @param accountId
     * @param outBizNo
     * @param amount
     * @param remark
     * @return
     */
    AlipayFundTransToaccountTransferResponse transToaccount(String accountId, String outBizNo, String amount, String remark);

    /**
     * 查询转账结果
     * @param out_biz_no
     * @param order_id
     * @return
     */
    BaseResponse query(String out_biz_no, String order_id);
    BaseResponse changed(String out_biz_no, String order_id);
}
