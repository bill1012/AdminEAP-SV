package com.eap.common.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-21
 */
public class LogInfo implements Serializable {
    private String menu;

    private String operation;

    private String uri;


    private Date createTime;

    private String createUser;

    private String createName;

    private String createHost;

    public LogInfo(String menu, String operation, String uri,  Date createTime, String createUser, String createName, String createHost) {
        this.menu = menu;
        this.operation = operation;
        this.uri = uri;
        this.createTime = createTime;
        this.createUser = createUser;
        this.createName = createName;
        this.createHost = createHost;
    }

    public LogInfo() {
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }
}
