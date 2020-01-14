package com.sinafinance.dao.mappers;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/8 10:55
 */

public interface MyBaseMapper <T> extends Mapper<T>, MySqlMapper<T> {
}
