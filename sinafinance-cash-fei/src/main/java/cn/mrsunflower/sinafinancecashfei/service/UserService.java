package cn.mrsunflower.sinafinancecashfei.service;

import cn.mrsunflower.sinafinancecashfei.pojo.User;
import com.github.pagehelper.PageInfo;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/8 11:56
 */

public interface UserService {
    PageInfo findAllUser(int pageNum, int pageSize);
    User findByUsername(String username);
}