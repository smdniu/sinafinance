package com.sinafinance.enums;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
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
