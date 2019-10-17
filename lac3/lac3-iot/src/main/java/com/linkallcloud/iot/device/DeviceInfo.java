package com.linkallcloud.iot.device;

public class DeviceInfo extends DeviceMessage {
    private static final long serialVersionUID = 5830547431281583808L;

    private String info;// 消息原始内容

    public DeviceInfo() {
        super();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
