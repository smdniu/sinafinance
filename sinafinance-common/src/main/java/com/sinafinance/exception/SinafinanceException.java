package com.sinafinance.exception;

/**
 * 系统异常
 *
 * @author liuruizhi
 * @since 2018/11/26
 */
public class SinafinanceException extends RuntimeException {

    private String code;

    public SinafinanceException() {
        super();
    }

    public SinafinanceException(Throwable throwable) {
        super(throwable);
    }

    public SinafinanceException(String msg) {
        super(msg);
    }

    public SinafinanceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public SinafinanceException(String code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    public SinafinanceException(String code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
