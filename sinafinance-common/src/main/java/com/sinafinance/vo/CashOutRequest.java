package com.sinafinance.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/9 17:52
 */

public class CashOutRequest {
    @Override
    public String toString() {
        return "CashOutRequest{" +
                "uid='" + uid + '\'' +
                ", money=" + money +
                ", bandAccountType='" + bandAccountType + '\'' +
                ", bandAccountId='" + bandAccountId + '\'' +
                ", payPassword='" + payPassword + '\'' +
                '}';
    }

    //当前用户
    private String uid;
    //提现金额
    @NotNull
    private BigDecimal money;
    @NotNull
    private String bandAccountType;
    @NotNull
    private String bandAccountId;
    @NotNull
    private String payPassword;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }


    public String getBandAccountType() {
        return bandAccountType;
    }

    public void setBandAccountType(String bandAccountType) {
        this.bandAccountType = bandAccountType;
    }

    public String getBandAccountId() {
        return bandAccountId;
    }

    public void setBandAccountId(String bandAccountId) {
        this.bandAccountId = bandAccountId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}
