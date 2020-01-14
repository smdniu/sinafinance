package com.sinafinance.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/9 18:42
 */

public class Account {
    private String id;
    private String uid;//用户登录账号
    private String password;//登录密码 -- 加密
    private String payPassword;//支付密码 -- 加密
    private BigDecimal money;//余额
    private String state;//normal 1:正常  freeze 2:冻结
    private String bandAccountType;//绑定的账户类型 1：支付宝 2：微信 3：银行卡
    private String bandAccount;//绑定的提现账户 -- 银行账户/支付宝账户，本系统提供支付宝账户
    private Date updateTime;//上次账户变更时间
    private Date createTime;//注册时间
    private Integer deleted;//0:未删除 1：删除

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBandAccountType() {
        return bandAccountType;
    }

    public void setBandAccountType(String bandAccountType) {
        this.bandAccountType = bandAccountType;
    }

    public String getBandAccount() {
        return bandAccount;
    }

    public void setBandAccount(String bandAccount) {
        this.bandAccount = bandAccount;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
