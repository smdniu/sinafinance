package com.sinafinance.service;

import com.github.pagehelper.PageInfo;
import com.sinafinance.pojo.User;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/8 11:56
 */

public interface UserService {
    PageInfo findAllUser(int pageNum, int pageSize);
    User findByUsername(String username);
}