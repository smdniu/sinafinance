package com.sinafinance.enums;
/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
 */
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
