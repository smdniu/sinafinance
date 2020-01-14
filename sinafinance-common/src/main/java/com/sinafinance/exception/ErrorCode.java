package com.sinafinance.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 错误码
 *
 * @author liuruizhi
 * @since 2018/11/26
 */
public enum ErrorCode {

    SUCCESS("成功", "00"),
    SYSTEM_ERROR("系统异常", "01"),
    TRANSACTION_FAIL("事务处理失败", "02"),
    FAIL("失败", "99"),

    // 基本错误0001~1000
    INTERNAL_ERROR("internal error", "0001"),
    PARAM_ERROR("参数错误", "0002"),
    INVOKE_XIYINZI_ERROR("远程调用接口异常", "0003"),
    EMPTY_RESPONSE_RESULT("返回结果为空","0004"),

    // 提现功能错误码
    PAY_PASSWD_WRONG("支付密码错误，请重新输入。", "10001"),


    // 第三方支付接口错误
    ALIPAY_ERROR("第三方支付接口调用失败","20001"),
    ALIPAY_RESP_NULL("第三方支付接口返回值为空","20002"),
    ALIPAY_RESP_CODE_ERROR("第三方支付接口返回值状态码错误","20003"),
    ALIPAY_RESP_MSG_ERROR("第三方支付接口返回值信息不正确","20004"),


    ;

    private String desc;
    private String code;

    private ErrorCode(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    public static ErrorCode getByCode(String code) {
        ErrorCode[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ErrorCode errorCode = var1[var3];
            if (StringUtils.equals(errorCode.getCode(), code)) {
                return errorCode;
            }
        }

        throw new IllegalArgumentException("ErrorCode's code cannot be null.");
    }

    public String getName() {
        return this.name();
    }

    public String getDesc() {
        return this.desc;
    }

    public String getCode() {
        return this.code;
    }
}
