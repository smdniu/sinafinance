package com.sinafinance.dao.mapper;

import com.sinafinance.dao.mappers.MyBaseMapper;
import com.sinafinance.pojo.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface AccountMapper extends MyBaseMapper<Account> {


    /**
     * 扣减余额方法
     * @param uid
     * @param payMoney
     */
    @Select("update tb_account set UPDATED_TIME_TM=now(),money=money-#{payMoney} where uid=#{uid}")
    void reduceMoney(@Param("uid") String uid, @Param("payMoney") BigDecimal payMoney);

}
