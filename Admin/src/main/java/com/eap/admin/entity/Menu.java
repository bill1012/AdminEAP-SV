package com.eap.admin.entity;

import com.eap.common.constant.CommonConstant;

import javax.persistence.*;
import java.util.Date;

@Table(name = "base_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 标题
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 菜单上下级关系
     */
    private String levelcode;

    /**
     * 父级节点
     */
    @Column(name = "parent_id")
    private String parentId= CommonConstant.ROOT;

    /**
     * 资源路径
     */
    private String href;

    /**
     * 前端组件
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    private String type;

    /**
     * 描述
     */
    private String description;

    /**
     * 启用禁用
     */
    private String enabled;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "create_name")
    private String createName;

    @Column(name = "create_host")
    private String createHost;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_name")
    private String updateName;

    @Column(name = "update_host")
    private String updateHost;


    @Transient
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取标题
     *
     * @return name - 标题
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标题
     *
     * @param name 标题
     */
    public void setName(String name) {
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取菜单上下级关系
     *
     * @return levelcode - 菜单上下级关系
     */
    public String getLevelcode() {
        return levelcode;
    }

    /**
     * 设置菜单上下级关系
     *
     * @param levelcode 菜单上下级关系
     */
    public void setLevelcode(String levelcode) {
        this.levelcode = levelcode;
    }

    /**
     * 获取父级节点
     *
     * @return parent_id - 父级节点
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置父级节点
     *
     * @param parentId 父级节点
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取资源路径
     *
     * @return href - 资源路径
     */
    public String getHref() {
        return href;
    }

    /**
     * 设置资源路径
     *
     * @param href 资源路径
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * 获取前端组件
     *
     * @return component - 前端组件
     */
    public String getComponent() {
        return component;
    }

    /**
     * 设置前端组件
     *
     * @param component 前端组件
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 获取图标
     *
     * @return icon - 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取启用禁用
     *
     * @return enabled - 启用禁用
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * 设置启用禁用
     *
     * @param enabled 启用禁用
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return create_name
     */
    public String getCreateName() {
        return createName;
    }

    /**
     * @param createName
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }

    /**
     * @return create_host
     */
    public String getCreateHost() {
        return createHost;
    }

    /**
     * @param createHost
     */
    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return update_user
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return update_name
     */
    public String getUpdateName() {
        return updateName;
    }

    /**
     * @param updateName
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    /**
     * @return update_host
     */
    public String getUpdateHost() {
        return updateHost;
    }

    /**
     * @param updateHost
     */
    public void setUpdateHost(String updateHost) {
        this.updateHost = updateHost;
    }
}