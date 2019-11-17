package com.eap.admin.vo;

import com.eap.common.vo.TreeNode;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-12
 */
public class MenuTree extends TreeNode {
    private String icon;
    private String href;
    private String path;
    private String component;
    private String redirect;
    private String label;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
