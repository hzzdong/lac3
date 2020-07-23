package com.linkallcloud.core.laclog;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.Domain;

public abstract class BaseLog extends Domain {
    private static final long serialVersionUID = -6428103666648336600L;

    /**
     * 是否已经被存储（可能会有多个拦截器拦截到该记录，如果已经被记录后续就不在重复记录）
     */
    @JSONField(serialize = false, deserialize = false)
    private boolean alreadyStored;
    @JSONField(serialize = false, deserialize = false)
    private Throwable error;// 方法自行失败的错误
    /**
     * 应用名
     */
    private String appName;
    /**
     * 应用模块
     */
    private String appType;

    private Long dtTime;

    public BaseLog() {
    }

    public BaseLog(Long id) {
        super(id);
    }

    public BaseLog(Long id, String uuid) {
        super(id, uuid);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public boolean isAlreadyStored() {
        return alreadyStored;
    }

    public void setAlreadyStored(boolean alreadyStored) {
        this.alreadyStored = alreadyStored;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Long getDtTime() {
        return dtTime;
    }

    public void setDtTime(Long dtTime) {
        this.dtTime = dtTime;
    }

}
