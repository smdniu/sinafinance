package com.sinafinance.service;


import com.sinafinance.pojo.DealLog;
import com.sinafinance.vo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * DealLog业务逻辑层
 */
public interface DealLogService {


    public List<DealLog> findAll();


    public PageResult<DealLog> findPage(int page, int size);


    public List<DealLog> findList(Map<String, Object> searchMap);


    public PageResult<DealLog> findPage(Map<String, Object> searchMap, int page, int size);


    public DealLog findById(String id);

    public void add(DealLog dealLog);


    public void update(DealLog dealLog);


    public void delete(String id);

}
