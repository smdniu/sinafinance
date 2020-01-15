package cn.mrsunflower.sinafinancecashfei.controller;

import cn.mrsunflower.sinafinancecashfei.service.WithdrawalService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CheckWithdrawRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WithdrawalCheckController {
    @Autowired
    private WithdrawalService withdrawalService;

    /**
     * 提现请求审核
     * @param userId
     * @param withdrawalId
     * @param type
     * @return
     */
    @PostMapping("/WithdrawalCheck/check")
    public BaseResponse check(@RequestBody CheckWithdrawRequest request) {
        BaseResponse check = withdrawalService.check(request);
        return check;
    }

}
