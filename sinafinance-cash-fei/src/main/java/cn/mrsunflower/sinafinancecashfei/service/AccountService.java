package cn.mrsunflower.sinafinancecashfei.service;


import cn.mrsunflower.sinafinancecashfei.pojo.Account;

import java.math.BigDecimal;

/**
 * Account业务逻辑层
 */
public interface AccountService {

    Account findById(String id);

    /**
     * 批量扣减库存
     * @param uid
     * @param uid
     */
    BigDecimal findAccountByUid(long uid);

}
