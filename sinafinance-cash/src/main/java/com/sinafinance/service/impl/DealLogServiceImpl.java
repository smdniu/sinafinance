package com.sinafinance.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sinafinance.pojo.DealLog;
import com.sinafinance.dao.mapper.DealLogMapper;
import com.sinafinance.service.DealLogService;
import com.sinafinance.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service("dealLogService")
public class DealLogServiceImpl implements DealLogService {

    @Autowired
    private DealLogMapper dealLogMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<DealLog> findAll() {
        return dealLogMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<DealLog> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<DealLog> DealLogs = (Page<DealLog>) dealLogMapper.selectAll();
        return new PageResult<DealLog>(DealLogs.getTotal(),DealLogs.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<DealLog> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return dealLogMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<DealLog> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<DealLog> DealLogs = (Page<DealLog>) dealLogMapper.selectByExample(example);
        return new PageResult<DealLog>(DealLogs.getTotal(),DealLogs.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public DealLog findById(String id) {
        return dealLogMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param DealLog
     */
    public void add(DealLog DealLog) {
        dealLogMapper.insert(DealLog);
    }

    /**
     * 修改
     * @param DealLog
     */
    public void update(DealLog DealLog) {
        dealLogMapper.updateByPrimaryKeySelective(DealLog);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(String id) {
        dealLogMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(DealLog.class);
        Example.Criteria criteria = example.createCriteria();
//        if(searchMap!=null){
//            // ID
//            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
//                criteria.andLike("id","%"+searchMap.get("id")+"%");
//            }
//            // 操作员
//            if(searchMap.get("operater")!=null && !"".equals(searchMap.get("operater"))){
//                criteria.andLike("operater","%"+searchMap.get("operater")+"%");
//            }
//            // 订单状态
//            if(searchMap.get("orderStatus")!=null && !"".equals(searchMap.get("orderStatus"))){
//                criteria.andLike("orderStatus","%"+searchMap.get("orderStatus")+"%");
//            }
//            // 付款状态
//            if(searchMap.get("payStatus")!=null && !"".equals(searchMap.get("payStatus"))){
//                criteria.andLike("payStatus","%"+searchMap.get("payStatus")+"%");
//            }
//            // 发货状态
//            if(searchMap.get("consignStatus")!=null && !"".equals(searchMap.get("consignStatus"))){
//                criteria.andLike("consignStatus","%"+searchMap.get("consignStatus")+"%");
//            }
//            // 备注
//            if(searchMap.get("remarks")!=null && !"".equals(searchMap.get("remarks"))){
//                criteria.andLike("remarks","%"+searchMap.get("remarks")+"%");
//            }
//
//
//        }
        return example;
    }

}
