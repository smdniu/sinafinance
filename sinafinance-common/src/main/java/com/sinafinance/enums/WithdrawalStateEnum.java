package com.sinafinance.enums;

import com.sinafinance.exception.SinafinanceException;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
 */
public enum WithdrawalStateEnum {

    //交易状态 1：发起提现 2：处理中 3：到账 4：提现失败 5：退回成功
    APPLY_WITHDRAWAL(1,"发起提现"),
    PROCESSING(2,"处理中"),
    SUCCESS(3,"成功"),
    FAILURE(4,"失败"),
    REFUND(5,"退款成功")

    ;
    private long code;
    private String desc;

    WithdrawalStateEnum(long code, String desc){
        this.code = code;
        this.desc = desc;
    }
    public static WithdrawalStateEnum getByCode(long code){
        for (WithdrawalStateEnum item : WithdrawalStateEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        throw new SinafinanceException();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
