package com.sinafinance.vo;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

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
                ", withdrawApplyTotal=" + withdrawApplyTotal +
                ", withdrawCharge=" + withdrawCharge +
                ", withdrawBankId='" + withdrawBankId + '\'' +
                ", payPassword='" + payPassword + '\'' +
                '}';
    }

    //当前用户
    private String uid;
    //申请提现金额
    @NotNull
    private BigDecimal withdrawApplyTotal;
    //提现手续费
    @NotNull
    private BigDecimal withdrawCharge;
    @NotNull
    private String withdrawBankId;//卡号
    @NotNull
    private String payPassword;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getWithdrawApplyTotal() {
        return withdrawApplyTotal;
    }

    public void setWithdrawApplyTotal(BigDecimal withdrawApplyTotal) {
        this.withdrawApplyTotal = withdrawApplyTotal;
    }

    public BigDecimal getWithdrawCharge() {
        return withdrawCharge;
    }

    public void setWithdrawCharge(BigDecimal withdrawCharge) {
        this.withdrawCharge = withdrawCharge;
    }

    public String getWithdrawBankId() {
        return withdrawBankId;
    }

    public void setWithdrawBankId(String withdrawBankId) {
        this.withdrawBankId = withdrawBankId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}
