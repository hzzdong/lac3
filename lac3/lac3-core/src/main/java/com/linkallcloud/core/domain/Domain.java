package com.linkallcloud.core.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.dto.born.DefaultDtoBorning;
import com.linkallcloud.core.dto.born.DtoBorning;
import com.linkallcloud.core.dto.born.DtoBorns;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.vo.Vo;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.UUID;

public abstract class Domain implements IDomain {
    private static final long serialVersionUID = 5910672863713144935L;

    @JSONField(serialize = false)
    protected static Log log = Logs.get();

    /**
     * 实体编号（唯一标识）
     */
    protected Long id;
    protected String uuid;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime; // 创建时间
    // protected PK createUserId;// 创建者id

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime; // 更新时间
    // protected PK updateUserId;// 更新者id

    protected int status; // 状态

    public Domain() {
        super();
        this.status = STATUS_NORMAL;
        this.uuid = generateUuid();
        this.createTime = new Date();
    }

    public Domain(Long id) {
        this();
        this.id = id;
        this.uuid = generateUuid();
    }

    public Domain(Long id, String uuid) {
        this();
        this.id = id;
        this.uuid = uuid;
    }

    public <V extends Vo> Domain(V vo) {
        this();
        BeanUtils.copyProperties(vo, this);
    }

    public <V extends Vo> V toVo(Class<V> classV) {
        Mirror<V> mirror = Mirror.me(classV);
        try {
            V vo = mirror.born();
            BeanUtils.copyProperties(this, vo);
            return vo;
        } catch (Throwable e) {
        }
        return null;
    }

    /**
     * Domain 转换成 Vo,根据提供的方法和构造参数，自动查找合适的静态构造方法或者构造器.
     *
     * @param domain
     * @param classV
     * @return
     */
    public static <E extends Domain, V extends Vo> V domain2Vo(E domain, Class<V> classV) {
        return Domain.domain2Vo(domain, classV, null, new Object[]{domain});
    }

    /**
     * Domain 转换成 Vo,根据提供的方法和构造参数，自动查找合适的静态构造方法或者构造器.
     *
     * @param domain
     * @param classV
     * @param methodName
     * @param methodArgs
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E extends Domain, V extends Vo> V domain2Vo(E domain, Class<V> classV,
                                                               String methodName, Object[] methodArgs) {
        if (domain != null && classV != null) {
            if (matchDomainArg(methodArgs, domain)) {
                Class<?>[] createArgTypes = Mirror.evalToTypes(methodArgs);
                DtoBorning borning = DtoBorns.evalBorning(classV, domain, methodName, createArgTypes);
                if (null != borning) {
                    if (borning instanceof DefaultDtoBorning) {
                        try {
                            return domain.toVo(classV);
                        } catch (Throwable e1) {
                            log.error(e1.getMessage(), e1);
                        }
                    } else {
                        try {
                            return (V) borning.born(methodArgs);
                        } catch (Throwable e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            } else {
                Mirror<V> mirror = Mirror.me(classV);
                try {
                    V vo = mirror.born();
                    BeanUtils.copyProperties(domain, vo);
                    return vo;
                } catch (Throwable e) {
                }
            }
        }
        return null;
    }

    /**
     * Vo 转换成 Domain,根据提供的方法和构造参数，自动查找合适的静态构造方法或者构造器.
     *
     * @param vo
     * @param classD
     * @return
     */
    public static <E extends Domain, V extends Vo> E vo2Domain(V vo, Class<E> classD) {
        return Domain.vo2Domain(vo, classD, null, new Object[]{vo});
    }

    /**
     * Vo 转换成 Domain,根据提供的方法和构造参数，自动查找合适的静态构造方法或者构造器.
     *
     * @param vo
     * @param classD
     * @param methodName
     * @param methodArgs
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E extends Domain, V extends Vo> E vo2Domain(V vo, Class<E> classD,
                                                               String methodName, Object[] methodArgs) {
        if (vo != null && classD != null) {
            if (matchDomainArg(methodArgs, vo)) {
                Class<?>[] createArgTypes = Mirror.evalToTypes(methodArgs);
                DtoBorning borning = DtoBorns.evalBorning(classD, vo, methodName, createArgTypes);
                if (null != borning) {
                    try {
                        return (E) borning.born(methodArgs);
                    } catch (Throwable e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } else {
                Mirror<E> mirror = Mirror.me(classD);
                try {
                    E domain = mirror.born();
                    BeanUtils.copyProperties(vo, domain);
                    return domain;
                } catch (Throwable e) {
                }
            }
        }
        return null;
    }

    /**
     * 检查参数中是否匹配到参数obj
     *
     * @param args
     * @param obj
     * @return true or false
     */
    private static boolean matchDomainArg(Object[] args, Object obj) {
        if (null == obj || null == args || args.length <= 0) {
            return false;
        }
        for (Object arg : args) {
            if (arg == obj) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean isValid() {
        return this.status == 0;
    }

    @Override
    public String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        IDomain that = (IDomain) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
