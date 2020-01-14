package cn.mrsunflower.sinafinancecashfei.controller;

import cn.mrsunflower.sinafinancecashfei.service.WithdrawalService;
import com.sinafinance.vo.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WithdrawalCheckController {
    @Autowired
    private WithdrawalService withdrawalService;

    @GetMapping("/WithdrawalCheck/check")
    public BaseResponse check(Long userId, Long cashID,int type) {
        BaseResponse check = withdrawalService.check(userId, cashID, type);
        return check;
    }

}
