package com.sinafinance.annotation;


import com.sinafinance.enums.LoggerFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuruizhi
 * @since 2018/11/26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggerOut {

    /**
     * 是否关闭日志输出功能
     *
     * @return
     */
    boolean enable() default true;
    /**
     * 是否输出执行时间
     *
     * @return
     */
    boolean duration() default true;
    /**
     * 是否只在debug输出日志
     */
    boolean debug() default false;

    boolean onlyReq() default false;

    LoggerFormat format() default LoggerFormat.JSON;
}
