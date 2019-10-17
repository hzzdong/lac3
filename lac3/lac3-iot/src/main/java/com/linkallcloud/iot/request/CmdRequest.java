package com.linkallcloud.iot.request;

import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.lang.Strings;

public class CmdRequest {

    private String tid;// 消息流水
    private String sourceId;// 请求源ID
    private String destId;// 目标ID
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date dt;// 请求时间
    private String type;// 请求类型
    private String content;// 请求内容

    public CmdRequest() {
        super();
        this.tid = UUID.randomUUID().toString().replace("-", "");
        this.dt = new Date();
    }

    public CmdRequest(String sourceId, String destId, String type, String content) {
        this();
        this.sourceId = sourceId;
        this.destId = destId;
        this.type = type;
        this.content = content;
    }
    
    public void check() throws BaseException {
        if(Strings.isBlank(tid)) {
            throw new BaseException("300001", "消息流水 tid 不能为空");
        }
        if(Strings.isBlank(sourceId)) {
            throw new BaseException("300002", "请求源 sourceId 不能为空");
        }
        if(Strings.isBlank(destId)) {
            throw new BaseException("300003", "请求目标 destId 不能为空");
        }
        if(Strings.isBlank(type)) {
            throw new BaseException("300004", "请求类型 type 不能为空");
        }
        if(Strings.isBlank(content)) {
            throw new BaseException("300005", "请求内容 content 不能为空");
        }
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
