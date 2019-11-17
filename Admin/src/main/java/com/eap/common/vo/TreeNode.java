package com.eap.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-12
 */
public class TreeNode {

    private String id;

    private String parentId;

    private String name;

    private String code;

    private List<TreeNode> children=new ArrayList<TreeNode>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void add(TreeNode node){
        children.add(node);
    }
}
