package com.linkallcloud.iot.device;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;

public class DeviceMessage implements Serializable {
    private static final long serialVersionUID = 3784951857869361538L;

    private String tid;// 消息流水号

    private String deviceId;// 设备ID
    private String type;// 消息类型
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date dt;// 消息时间

    public DeviceMessage() {
        super();
        this.setDt(new Date());
        this.setTid(this.generateTid());
    }
    
    public String generateTid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

}
