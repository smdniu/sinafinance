package com.sinafinance.controller;

import com.sinafinance.service.UserService;
import com.sinafinance.vo.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/findAllUser")
    public BaseResponse findAllUser(@RequestParam int pageNo,@RequestParam int pageSize){
        return BaseResponse.newSuccResponse(userService.findAllUser(pageNo, pageSize));
    }

    @RequestMapping("/findByUsername")
    public BaseResponse findByUsername(@RequestParam String username){
        return BaseResponse.newSuccResponse(userService.findByUsername(username));
    }
}
