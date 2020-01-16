package com.sinafinance.cashout.controller;

import com.sinafinance.cashout.service.WithdrawalService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.NotifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 回调接口
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
 */
@RestController
public class WithdrawalNotifyController {
    @Autowired
    private WithdrawalService withdrawalService;

    @RequestMapping("/notify")
    public BaseResponse notifyLogic(@RequestBody NotifyRequest request) {
       return withdrawalService.notifyLogic(request);
    }

}
