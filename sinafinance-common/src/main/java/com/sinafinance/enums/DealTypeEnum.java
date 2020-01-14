package com.sinafinance.enums;

import com.sinafinance.exception.SinafinanceException;
import org.apache.commons.lang3.StringUtils;

public enum DealTypeEnum{

    //交易类型 1：提现 2：充值
    CASH_OUT("1","提现"),
    CASH_IN("2","充值")
    ;

    private String code;
    private String desc;

    DealTypeEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
    public static DealTypeEnum getByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (DealTypeEnum item : DealTypeEnum.values()) {
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
