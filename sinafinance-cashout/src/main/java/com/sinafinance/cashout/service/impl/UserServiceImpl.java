package com.sinafinance.cashout.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sinafinance.cashout.mapper.UserMapper;
import com.sinafinance.pojo.User;
import com.sinafinance.cashout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/8 11:56
 */

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;//这里会报错，但是并不会影响

    @Override
    public PageInfo findAllUser(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectAll();
        PageInfo pageInfo = new PageInfo(users);
        return pageInfo;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

}
