package com.sinafinance.vo;

/**
 * author v_sunfengxiao
 * date 2019/4/24
 */
public class UserVo {
    /**
     * Sso用户名
     */
    private String ssoUsername;

    /**
     * 本地用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String name;

    public String getSsoUsername() {
        return ssoUsername;
    }

    public void setSsoUsername(String ssoUsername) {
        this.ssoUsername = ssoUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}