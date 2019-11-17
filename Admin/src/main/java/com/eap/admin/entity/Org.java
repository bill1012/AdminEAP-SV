package com.eap.admin.entity;



import javax.persistence.*;

@Table(name = "base_org")
public class Org {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    /**
     * 编码  唯一校验  
     */
    private String code;

    /**
     * 层级编码
     */
    @Column(name = "level_code")
    private String levelCode;

    /**
     * 数据字典ID
     */
    @Column(name = "org_type")
    private String orgType;

    /**
     * Y删除 N不删除
     */
    private String deleted;

    /**
     * 备注
     */
    private String note;

    /**
     * 上级ID
     */
    private String parentid;

    private String lspell;
    private String initials;
    private String uspell;

    public String getLspell() {
        return lspell;
    }

    public void setLspell(String lspell) {
        this.lspell = lspell;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getUspell() {
        return uspell;
    }

    public void setUspell(String uspell) {
        this.uspell = uspell;
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
     * 获取编码  唯一校验  
     *
     * @return code - 编码  唯一校验  
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编码  唯一校验  
     *
     * @param code 编码  唯一校验  
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取层级编码
     *
     * @return level_code - 层级编码
     */
    public String getLevelCode() {
        return levelCode;
    }

    /**
     * 设置层级编码
     *
     * @param levelCode 层级编码
     */
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    /**
     * 获取数据字典ID
     *
     * @return org_type - 数据字典ID
     */
    public String getOrgType() {
        return orgType;
    }

    /**
     * 设置数据字典ID
     *
     * @param orgType 数据字典ID
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    /**
     * 获取Y删除 N不删除
     *
     * @return deleted - Y删除 N不删除
     */
    public String getDeleted() {
        return deleted;
    }

    /**
     * 设置Y删除 N不删除
     *
     * @param deleted Y删除 N不删除
     */
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取备注
     *
     * @return note - 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置备注
     *
     * @param note 备注
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取上级ID
     *
     * @return parentid - 上级ID
     */
    public String getParentid() {
        return parentid;
    }

    /**
     * 设置上级ID
     *
     * @param parentid 上级ID
     */
    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}