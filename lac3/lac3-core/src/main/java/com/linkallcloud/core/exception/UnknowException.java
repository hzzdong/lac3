package com.linkallcloud.core.exception;

public class UnknowException extends BaseException {
    private static final long serialVersionUID = 1517915921538760778L;

    public UnknowException() {
        this(Exceptions.CODE_ERROR_UNKNOW);
    }

    public UnknowException(String code, Object[] args) {
        super(code, args);
    }

    public UnknowException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public UnknowException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UnknowException(String code, String message) {
        super(code, message);
    }

    public UnknowException(String code, Throwable cause) {
        super(code, cause);
    }

    public UnknowException(String code) {
        super(code);
    }


}
