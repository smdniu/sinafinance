package com.sinafinance.enums;

public enum AlipayTradeStatus {
    TRADE_SUCCESS("sucess"),
    TRADE_FINISHED("finished"),
    ;
    private String status;

    AlipayTradeStatus(String status){
        this.status = status;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
