package com.sinafinance.enums;

/**
 * @description:
 * EnumType
 *  枚举类可以实现该接口，以便进行反序列化
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
 */
public interface EnumType<T> {

    T getCode();
}
