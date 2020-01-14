package com.sinafinance.service;


import com.sinafinance.pojo.Account;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CashOutRequest;

import java.math.BigDecimal;

/**
 * Account业务逻辑层
 */
public interface AccountService {

    BaseResponse cashOut(CashOutRequest request);
    Account findById(String id);

    /**
     * 批量扣减库存
     * @param uid
     * @param payMoney
     */
    boolean reduceAccount(String uid,BigDecimal payMoney);

}
