package cn.mrsunflower.sinafinancecashfei.controller;

import cn.mrsunflower.sinafinancecashfei.service.UserService;
import com.sinafinance.vo.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/8 11:58
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/findAllUser")
    public BaseResponse findAllUser(@RequestParam int pageNo,@RequestParam int pageSize){
        return BaseResponse.newSuccResponse(userService.findAllUser(pageNo, pageSize));
    }

    @PostMapping("/findByUsername")
    public BaseResponse findByUsername(@RequestParam(value = "username") String username){
        return BaseResponse.newSuccResponse(userService.findByUsername(username));
    }
}
