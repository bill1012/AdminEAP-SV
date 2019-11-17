package com.eap.common.vo;

import java.io.Serializable;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-21
 */
public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -2445943548965154778L;

    private String username;
    private String password;

    public JwtAuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public JwtAuthenticationRequest(){

    }


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
