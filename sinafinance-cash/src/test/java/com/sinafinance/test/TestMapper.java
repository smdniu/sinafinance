package com.sinafinance.test;

import com.sinafinance.pojo.User;
import com.sinafinance.dao.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/8 10:57
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMapper {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testSelectAll(){
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }
}
