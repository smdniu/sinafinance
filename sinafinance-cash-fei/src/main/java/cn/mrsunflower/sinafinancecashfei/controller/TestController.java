package cn.mrsunflower.sinafinancecashfei.controller;

import cn.mrsunflower.sinafinancecashfei.pojo.WithdrawalInfo;
import cn.mrsunflower.sinafinancecashfei.pojo.WithdrawalBank;
import com.sinafinance.vo.CashOutRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("test")
    public WithdrawalBank test() {
        return new WithdrawalBank();
    }

    @GetMapping("test2")
    public WithdrawalInfo test1() {
        return new WithdrawalInfo();
    }
    @GetMapping("test3")
    public CashOutRequest test3() {
        return new CashOutRequest();
    }

}
