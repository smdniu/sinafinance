package com.sinafinance.cashout.controller;

import com.sinafinance.cashout.service.UserService;
import com.sinafinance.vo.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
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

    //#######################################下面是依赖的bean#######################################//

    private UserService userService;


    @PostMapping(value = "/findAllUser")
    public BaseResponse findAllUser(@RequestParam int pageNo, @RequestParam int pageSize) {
        return BaseResponse.newSuccResponse(userService.findAllUser(pageNo, pageSize));
    }

    @PostMapping("/findByUsername")
    public BaseResponse findByUsername(@RequestParam(value = "username") String username) {
        return BaseResponse.newSuccResponse(userService.findByUsername(username));
    }


    //#######################################下面是依赖的bean setter#######################################//

    @Autowired
    @Qualifier("userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
