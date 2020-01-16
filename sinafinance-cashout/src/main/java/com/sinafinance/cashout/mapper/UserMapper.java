package com.sinafinance.cashout.mapper;

import com.sinafinance.pojo.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    User findByUsername(String uid);
}
