package com.sinafinance.enums;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
 */
public enum ResponseCode {
    SUCCESS("20000"),//成功
    ERROR("50000"),//成功
    OBJECT_CONVERSION_ERROR("50001"),//对象转换错误
    PARAMETER_ERROR("50002"),//参数错误
    EXISTING_ERROR("50003"),//数据库已存在错误码
    SYSTEM_ERROR("50004")//系统错误
     ;
    private String code;

    public String getCode() {
        return code;
    }

    ResponseCode(String code) {
        this.code = code;
    }
}
