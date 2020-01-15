package com.sinafinance.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/9 17:52
 */
public class NotifyRequest {
    private String userId;
    private Long withdrawalId;
    private String withdrawOrder;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public String getWithdrawOrder() {
        return withdrawOrder;
    }

    public void setWithdrawOrder(String withdrawOrder) {
        this.withdrawOrder = withdrawOrder;
    }
}
