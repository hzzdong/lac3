package com.linkallcloud.core.laclog;

public class ServiceBusiLog extends BusiLog {
    private static final long serialVersionUID = 7855862585657490739L;

    private String ip;// 调用方ip

    public ServiceBusiLog() {
        super();
    }

    public ServiceBusiLog(Long id, String uuid) {
        super(id, uuid);
    }

    public ServiceBusiLog(Long id) {
        super(id);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
