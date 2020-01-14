package com.sinafinance.pojo;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/9 19:38
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录
 */
public class Deal {
    private String id;//主键id
    private String order_id;//交易流水号 唯一 由支付宝转账后响应
    private String uid;//用户
    private BigDecimal dealMoney;//交易金额
    private BigDecimal serverMoney;//服务费
    private String dealType;//交易类型 1：提现 2：充值
    private String dealState; //交易状态 1：发起提现 2：处理中 3：到账 4：提现失败 5：退回成功
    private Date updateTime;//交易变更时间
    private Date createTime;//申请时间
    private Date expectTime;//预计到账时间
    private Date endTime;//交易完成时间
    private Integer deleted;//0:未删除 1：删除

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(BigDecimal dealMoney) {
        this.dealMoney = dealMoney;
    }

    public BigDecimal getServerMoney() {
        return serverMoney;
    }

    public void setServerMoney(BigDecimal serverMoney) {
        this.serverMoney = serverMoney;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDealState() {
        return dealState;
    }

    public void setDealState(String dealState) {
        this.dealState = dealState;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
