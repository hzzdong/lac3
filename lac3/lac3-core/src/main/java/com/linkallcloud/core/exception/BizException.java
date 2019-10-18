package com.linkallcloud.core.exception;

public class BizException extends BaseRuntimeException {

    public BizException() {
        super();
    }

    public BizException(String code) {
        super(code);
    }

    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String code, Object[] args) {
        super(code, args);
    }

    public BizException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public BizException(String code, Throwable cause) {
        super(code, cause);
    }

    public BizException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
