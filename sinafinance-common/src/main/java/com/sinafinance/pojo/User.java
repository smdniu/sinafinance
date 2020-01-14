package com.sinafinance.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description:
 * @author: sunmengdi
 * @time: 2020/1/8 11:01
 */

@Table(name = "tb_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
