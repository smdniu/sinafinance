package com.sinafinance.enums;

import com.sinafinance.exception.SinafinanceException;
import org.apache.commons.lang3.StringUtils;

public enum DealStateEnum {

    //交易状态 1：发起提现 2：处理中 3：到账 4：提现失败 5：退回成功
    CREATE("1","发起提现"),
    DEALING("2","处理中"),
    SUCCESS("3","成功"),
    FAIL("4","失败"),
    REFUND("5","退款成功")

    ;
    private String code;
    private String desc;

    DealStateEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
    public static DealStateEnum getByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (DealStateEnum item : DealStateEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        throw new SinafinanceException();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
