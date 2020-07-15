package com.linkallcloud.web.exception;

import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;

public class LoginException extends BaseRuntimeException {
    private static final long serialVersionUID = 3171737248925270054L;

    private String loginUrl;

    public LoginException() {
        super(Exceptions.CODE_ERROR_AUTH_ACCOUNT);
    }

    public LoginException(String code, Object[] args) {
        super(code, args);
    }

    public LoginException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public LoginException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public LoginException(String code, String message) {
        super(code, message);
    }

    public LoginException(String code, Throwable cause) {
        super(code, cause);
    }

    public LoginException(String code) {
        super(code);
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

}
