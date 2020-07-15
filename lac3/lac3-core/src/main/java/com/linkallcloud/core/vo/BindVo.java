package com.linkallcloud.core.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.dto.Client;

public class BindVo extends Vo {
    private static final long serialVersionUID = -3538547899656945236L;

    private String loginName;
    @JSONField(serialize = false)
    private String password;
    private String userType;
    private String vcode;//validate code
    private Client client;

    private String fromId;//from app id
    private String from;//from app code
    private String service;//app url

    public BindVo() {
        super();
    }

    public BindVo(Object o) {
        super(o);
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
