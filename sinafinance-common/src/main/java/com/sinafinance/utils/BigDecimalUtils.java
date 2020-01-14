package com.sinafinance.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static BigDecimal sum(BigDecimal... params){
        BigDecimal value = BigDecimal.ZERO;
        for (BigDecimal param : params) {
            if (param != null){
                value = value.add(param);
            }
        }
        return value;
    }
}
