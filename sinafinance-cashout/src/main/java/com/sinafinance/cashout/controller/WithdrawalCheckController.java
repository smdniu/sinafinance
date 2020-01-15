package com.sinafinance.cashout.controller;

import com.sinafinance.cashout.service.WithdrawalService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CheckWithdrawRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WithdrawalCheckController {
    @Autowired
    private WithdrawalService withdrawalService;

    /**
     * 提现请求审核
     * @param request
     * @return
     */
    @PostMapping("/WithdrawalCheck/check")
    public BaseResponse check(@RequestBody CheckWithdrawRequest request) {
        BaseResponse check = withdrawalService.check(request);
        return check;
    }

}
