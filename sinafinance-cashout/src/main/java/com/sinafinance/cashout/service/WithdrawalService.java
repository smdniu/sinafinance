package com.sinafinance.cashout.service;


import com.sinafinance.pojo.WithdrawalInfo;
import com.sinafinance.vo.*;

import java.util.List;

public interface WithdrawalService {
    List<WithdrawalInfo> getWithdrawalByUserId(String userId);

    BaseResponse cashOut(CashOutRequest request);

    PageResult applyWithDrawalList(Long userId, int currentPageNum, int currentPageSize);

    WithdrawalInfo getWithdrawalByWithdrawalOrderAndStatus(String withdrawalOrder, String withdrawalStatus);

    /**
     * 审核提现申请
     * @param request
     *      *@param userId
     *      * @param withdrawalId
     *      * @param type 0 表示失败,1表示通过
     * @return
     */
    BaseResponse check(CheckWithdrawRequest request);

    BaseResponse notifyLogic(NotifyRequest request);
}
