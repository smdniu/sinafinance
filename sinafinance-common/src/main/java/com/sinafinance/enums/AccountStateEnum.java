package com.sinafinance.enums;

import com.sinafinance.exception.SinafinanceException;
import org.apache.commons.lang3.StringUtils;

public enum AccountStateEnum {
    //normal 1:正常  freeze 2:冻结
    NORMAL("1","正常"),
    FREEZE("2","冻结")
    ;
    private String code;
    private String desc;

    AccountStateEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
    public static AccountStateEnum getByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (AccountStateEnum item : AccountStateEnum.values()) {
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
