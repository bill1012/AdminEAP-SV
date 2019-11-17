package com.eap.admin.vo;

import com.eap.common.vo.PermissionInfo;

import java.util.List;

/**
 * @Author 鬼王
 * @Date 2018/04/18 13:39
 */
public class UserVo {

    public String id;

    public String username;

    public String name;

    private String image;

    private String description;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    private String header;

    private List<PermissionInfo> menus;

    private List<PermissionInfo> elements;

    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionInfo> getMenus() {
        return menus;
    }

    public void setMenus(List<PermissionInfo> menus) {
        this.menus = menus;
    }

    public List<PermissionInfo> getElements() {
        return elements;
    }

    public void setElements(List<PermissionInfo> elements) {
        this.elements = elements;
    }

}
