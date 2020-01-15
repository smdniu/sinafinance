package cn.mrsunflower.sinafinancecashfei.controller;

import cn.mrsunflower.sinafinancecashfei.service.WithdrawalService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.NotifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class WithdrawalNotifyController {
    @Autowired
    private WithdrawalService withdrawalService;

    @RequestMapping("/notify")
    public BaseResponse notifyLogic(@RequestBody NotifyRequest request) {
       return withdrawalService.notifyLogic(request);
    }

//    @LoggerOut
//    @GetMapping("/queryPayStatus")
//    public BaseResponse queryPayStatus(String id,String orderId){
//        return  withdrawalService.query(id,orderId);
//    }


}
