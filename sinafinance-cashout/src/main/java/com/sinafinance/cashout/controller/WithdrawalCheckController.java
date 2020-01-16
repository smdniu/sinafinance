package com.sinafinance.cashout.controller;

import com.sinafinance.annotation.LoggerOut;
import com.sinafinance.cashout.service.WithdrawalService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CheckWithdrawRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 提现审核类
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
 */
@RestController
public class WithdrawalCheckController {
    @Autowired
    private WithdrawalService withdrawalService;

    /**
     * 提现请求审核
     * @param request
     * @return
     */
    @LoggerOut
    @PostMapping("/WithdrawalCheck/check")
    public BaseResponse check(@RequestBody CheckWithdrawRequest request) {
        BaseResponse check = withdrawalService.check(request);
        return check;
    }

}
