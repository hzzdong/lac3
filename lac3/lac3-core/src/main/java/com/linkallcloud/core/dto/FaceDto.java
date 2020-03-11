package com.linkallcloud.core.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.IDomain;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.vo.Vo;
import org.springframework.beans.BeanUtils;

public abstract class FaceDto<T extends IDomain> extends Vo {
    private static final long serialVersionUID = 1779659772871707621L;

    @JSONField(serialize = false)
    private static Log log = Logs.get();

    @JSONField(serialize = false)
    protected Mirror<T> mirror;

    @JSONField(serialize = false)
    protected int versn; // 版本号

    @SuppressWarnings("unchecked")
    public FaceDto() {
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            log.warn("!!!Fail to get TypeParams for self!", e);
        }
    }

    @SuppressWarnings("unchecked")
    public FaceDto(T entity) {
        super(entity);
        if (entity != null) {
            this.mirror = (Mirror<T>) Mirror.me(entity.getClass());
        }
    }

    @JSONField(serialize = false)
    public Class<T> getEntityClass() {
        return mirror.getType();
    }

    @SuppressWarnings("unchecked")
    public <C extends T> void setEntityType(Class<C> classOfT) {
        mirror = (Mirror<T>) Mirror.me(classOfT);
    }

    @JSONField(serialize = false)
    public int getVersn() {
        return versn;
    }

    public void setVersn(int versn) {
        this.versn = versn;
    }

    @JSONField(serialize = false)
    public Mirror<T> getMirror() {
        return mirror;
    }

    /**
     * 功能同构造器，把Domain转换成DTO
     *
     * @param domain
     */
    @SuppressWarnings("unchecked")
    public void fromDomain(T domain) {
        if (null != domain) {
            try {
                if (null == this.mirror) {
                    this.mirror = (Mirror<T>) Mirror.me(domain.getClass());
                }
                BeanUtils.copyProperties(domain, this);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * DTO 转换成 Domain
     *
     * @return Domain
     */
    public T toDomain() {
        if (this.mirror == null) {
            return null;
        }
        T entity = null;
        try {
            entity = this.mirror.born();
            BeanUtils.copyProperties(this, entity);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return entity;
    }

}
