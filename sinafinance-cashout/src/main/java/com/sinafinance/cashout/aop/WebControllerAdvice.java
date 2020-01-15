/*
 * Copyright (C) 2016 Attractor, Inc. All Rights Reserved.
 */
package com.sinafinance.cashout.aop;

import com.google.common.base.Throwables;
import com.sinafinance.exception.ErrorCode;
import com.sinafinance.exception.SinafinanceException;
import com.sinafinance.exception.WithdrawalFailureException;
import com.sinafinance.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice for exceptions.
 */
@ControllerAdvice
public class WebControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(WebControllerAdvice.class);

    @ExceptionHandler(SinafinanceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleSinafinanceException(SinafinanceException exception) {
        logger.error("handle caas exception. due to error[{}]",
                Throwables.getStackTraceAsString(exception));
        return BaseResponse
                .newFailResponse(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(WithdrawalFailureException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleWithdrawalFailureException(WithdrawalFailureException exception) {
        logger.error("handle withdrawal failed exception. due to error[{}]",
                Throwables.getStackTraceAsString(exception));

        //执行回退操作
        //修改交易流水
        //生成日志
        //本地账户回退

        return BaseResponse
                .newFailResponse(exception.getCode(), exception.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleRuntimeException(RuntimeException runtimeException) {
        logger.error("handle runtime exception. due to error[{}]",
                Throwables.getStackTraceAsString(runtimeException));
        return BaseResponse
                .newFailResponse(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getDesc());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleException(Exception exception) {
        logger.error("handle checked exception. due to error[{}]",
                Throwables.getStackTraceAsString(exception));
        return BaseResponse
                .newFailResponse(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getDesc());
    }

}
