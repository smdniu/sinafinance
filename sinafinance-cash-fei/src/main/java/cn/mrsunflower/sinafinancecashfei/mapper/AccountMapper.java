package cn.mrsunflower.sinafinancecashfei.mapper;

import cn.mrsunflower.sinafinancecashfei.pojo.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

public interface AccountMapper extends Mapper<Account> {
}
