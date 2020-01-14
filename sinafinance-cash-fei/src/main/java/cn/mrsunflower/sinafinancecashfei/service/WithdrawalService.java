package cn.mrsunflower.sinafinancecashfei.service;

import cn.mrsunflower.sinafinancecashfei.pojo.WithdrawalInfo;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CashOutRequest;
import com.sinafinance.vo.PageResult;

import java.util.List;

public interface WithdrawalService {
    List<WithdrawalInfo> getWithdrawalByUserId(String  userId);

    BaseResponse cashOut(CashOutRequest request);

    PageResult applyWithDrawalList(Long userId, int currentPageNum, int currentPageSize);

    WithdrawalInfo getWithdrawalByWithdrawalOrderAndStatus(String withdrawalOrder,String withdrawalStatus );

    /**
     * 审核提现申请
     * @param userId
     * @param cashId
     * @param type 0 表示失败,1表示通过
     * @return
     */
    BaseResponse check(Long  userId,Long cashId,int type);

}
