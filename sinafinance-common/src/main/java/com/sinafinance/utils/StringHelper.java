package com.sinafinance.utils;

/**
 * 处理些类似{@link String#valueOf}的情况
 *
 * @author liuruizhi
 * @since 2019/4/3
 */
public class StringHelper {

    private static final String NULL = "null";

    public static String valueOf(Object string) {

        if (null == string) {
            return null;
        }

        if (NULL.equals(string.toString())) {
            return null;
        }

        return string.toString();
    }
}
