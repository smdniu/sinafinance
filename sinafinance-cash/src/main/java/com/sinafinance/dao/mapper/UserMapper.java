package com.sinafinance.dao.mapper;

import com.sinafinance.pojo.User;
import com.sinafinance.dao.mappers.MyBaseMapper;

public interface UserMapper extends MyBaseMapper<User> {
    User findByUsername(String username);
}
