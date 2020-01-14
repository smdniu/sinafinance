package com.sinafinance.enums;

public enum WithdrawalConstant {
    APPLY_WITHDRAWAL(1),//发起提现
    PROCESSING(2) ,//处理中
    FAILURE(4);//失败
    private long status;

    public long getStatus() {
        return status;
    }

    WithdrawalConstant(long status) {
        this.status = status;
    }
}
