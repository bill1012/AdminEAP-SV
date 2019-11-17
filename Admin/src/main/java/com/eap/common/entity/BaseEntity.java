package com.eap.common.entity;

import com.eap.common.annotation.SourceWay;
import com.eap.common.annotation.ValueInject;
import com.eap.common.annotation.ValueSource;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 鬼王
 * @Date: 2018/03/20 17:00
 */
@ValueInject
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * Create user id
     */
    @ValueSource(SourceWay.USERID)
    private String createUid;

    @ValueSource(SourceWay.SYSDATE)
    private Date createTime;

    /**
     * Client IP
     */
    @ValueSource(SourceWay.IP)
    private String createHost;

    /**
     * Modify user id
     */
    @ValueSource(value = SourceWay.USERID, update = true)
    private String modifyUid;

    @ValueSource(value = SourceWay.SYSDATE, update = true)
    private Date modifyTime;

    /**
     * Client IP
     */
    @ValueSource(value = SourceWay.IP, update = true)
    private String modifyHost;

    public BaseEntity() { }

    public BaseEntity(String createUid) {
        this.createUid = createUid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateUid() {
        return createUid;
    }

    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

    public String getModifyUid() {
        return modifyUid;
    }

    public void setModifyUid(String modifyUid) {
        this.modifyUid = modifyUid;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyHost() {
        return modifyHost;
    }

    public void setModifyHost(String modifyHost) {
        this.modifyHost = modifyHost;
    }
}
