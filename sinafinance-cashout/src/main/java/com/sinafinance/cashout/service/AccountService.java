package com.sinafinance.cashout.service;


import com.sinafinance.pojo.Account;

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
