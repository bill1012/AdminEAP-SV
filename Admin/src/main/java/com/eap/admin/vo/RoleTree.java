package com.eap.admin.vo;

import com.eap.common.vo.TreeNode;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-12
 */
public class RoleTree extends TreeNode {
    private String levelcode;
    private String description;

    public String getLevelcode() {
        return levelcode;
    }

    public void setLevelcode(String levelcode) {
        this.levelcode = levelcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
