package cn.mrsunflower.sinafinancecashfei.service;

import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.sinafinance.vo.BaseResponse;

import javax.servlet.http.HttpServletRequest;

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

    String callback(HttpServletRequest request);

    /**
     * 查询转账结果
     * @param out_biz_no
     * @param order_id
     * @return
     */
    BaseResponse query(String out_biz_no, String order_id);

    /**
     * 回调通知
     * @param out_biz_no
     * @param order_id
     * @return
     */
    BaseResponse changed(String out_biz_no, String order_id);
}
