package cn.mrsunflower.sinafinancecashfei.mapper;

import cn.mrsunflower.sinafinancecashfei.pojo.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    User findByUsername(String uid);
}
