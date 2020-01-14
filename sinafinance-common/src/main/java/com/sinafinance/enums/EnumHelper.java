package com.sinafinance.enums;

/**
 * EnumHelper
 *
 * @author liuruizhi
 * @since 2018/11/5
 */
public class EnumHelper {

    public EnumHelper() {
    }

    public static <T extends EnumType, V> T fromCode(V code, Class<T> clazz) {

        if (code == null) {
            return null;
        }

        for (T value : clazz.getEnumConstants()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }

        return null;
    }
}
