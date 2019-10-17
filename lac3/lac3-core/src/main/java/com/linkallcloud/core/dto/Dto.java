package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;

public abstract class Dto<PK extends Serializable, T extends Domain<PK>> extends FaceDto<PK, T> {
    private static final long serialVersionUID = -4583201108023551132L;

    /**
     * 实体编号（唯一标识）
     */
    protected PK id;
    protected String uuid;

    @JSONField(serialize = false, format = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime; // 创建时间
    @JSONField(serialize = false)
    protected Long createUserId;// 创建者id

    @JSONField(serialize = false, format = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime; // 更新时间
    @JSONField(serialize = false)
    protected PK updateUserId;// 更新者id

    protected int status; // 状态

    public Dto() {
        super();
    }

    public Dto(T entity) {
        super(entity);
    }

    public void generateUuid() {
        this.uuid = UUID.randomUUID().toString().replace("_", "");
    }

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JSONField(serialize = false)
    public int getVersn() {
        return versn;
    }

    public void setVersn(int versn) {
        this.versn = versn;
    }

    @JSONField(serialize = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JSONField(serialize = false)
    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    @JSONField(serialize = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @JSONField(serialize = false)
    public PK getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(PK updateUserId) {
        this.updateUserId = updateUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
