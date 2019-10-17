package com.linkallcloud.core.exception;

import com.linkallcloud.core.util.ResBundle;

public class BaseRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 609281876732234454L;

    private String code;

    public BaseRuntimeException() {
        this(Exceptions.CODE_ERROR_UNKNOW);
    }

    public BaseRuntimeException(String code) {
        this(code, ResBundle.getMessage(code));
    }

    public BaseRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseRuntimeException(String code, Object[] args) {
        this(code, ResBundle.getMessage(code), args);
    }

    public BaseRuntimeException(String code, String msgFormat, Object[] args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }

    public BaseRuntimeException(String code, Throwable cause) {
        this(code, ResBundle.getMessage(code), cause);
    }

    public BaseRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

}
