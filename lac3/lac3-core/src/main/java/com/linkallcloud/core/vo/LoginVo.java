package com.linkallcloud.core.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.dto.Client;

public class LoginVo extends Vo {
    private static final long serialVersionUID = 6105614965783729417L;

    private String loginName;
    @JSONField(serialize = false)
    private String password;
    private String userType;
    private Integer rememberMe;
    private String vcode;//validate code
    private String lt;//login ticket
    private Client client;
    private String base; // base path
    private String pwdStrength;//密码强度

    public LoginVo() {
        super();
    }

    public LoginVo(Object o) {
        super(o);
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @JSONField(serialize = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Integer rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getPwdStrength() {
        return pwdStrength;
    }

    public void setPwdStrength(String pwdStrength) {
        this.pwdStrength = pwdStrength;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
