package com.sinafinance.enums;

/**
 * EnumType
 * 枚举类可以实现该接口，以便进行反序列化
 *
 * @author liuruizhi
 * @since 2018/11/5
 */
public interface EnumType<T> {

    T getCode();
}
