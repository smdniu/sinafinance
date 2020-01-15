package com.sinafinance.exception;

/**
 * 系统异常
 *
 */
public class WithdrawalFailureException extends RuntimeException {

    private String code;

    public WithdrawalFailureException() {
        super();
    }

    public WithdrawalFailureException(Throwable throwable) {
        super(throwable);
    }

    public WithdrawalFailureException(String msg) {
        super(msg);
    }

    public WithdrawalFailureException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public WithdrawalFailureException(String code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    public WithdrawalFailureException(String code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
