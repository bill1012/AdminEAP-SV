package com.eap.admin.entity;

import javax.persistence.*;

@Table(name = "base_dict")
public class Dict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String code;

    private String name;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "level_code")
    private String levelCode;

    private String enabled;

    private String description;

    private String value;

    private String lspell;

    private String uspell;

    private String initials;

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
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return parent_id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return level_code
     */
    public String getLevelCode() {
        return levelCode;
    }

    /**
     * @param levelCode
     */
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    /**
     * @return enabled
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return lspell
     */
    public String getLspell() {
        return lspell;
    }

    /**
     * @param lspell
     */
    public void setLspell(String lspell) {
        this.lspell = lspell;
    }

    /**
     * @return uspell
     */
    public String getUspell() {
        return uspell;
    }

    /**
     * @param uspell
     */
    public void setUspell(String uspell) {
        this.uspell = uspell;
    }

    /**
     * @return initials
     */
    public String getInitials() {
        return initials;
    }

    /**
     * @param initials
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }
}