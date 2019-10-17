package com.linkallcloud.core.exception;

import com.linkallcloud.core.util.ResBundle;

public class DataBaseException extends BaseRuntimeException {
    private static final long serialVersionUID = 488374255363624256L;

    public DataBaseException() {
        super(Exceptions.CODE_ERROR_DB, ResBundle.getMessage(Exceptions.CODE_ERROR_DB));
    }

    public DataBaseException(Throwable cause) {
        super(Exceptions.CODE_ERROR_DB, ResBundle.getMessage(Exceptions.CODE_ERROR_DB), cause);
    }

    public DataBaseException(String code, Object[] args) {
        super(code, args);
    }

    public DataBaseException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public DataBaseException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public DataBaseException(String code, String message) {
        super(code, message);
    }

    public DataBaseException(String code, Throwable cause) {
        super(code, cause);
    }

    public DataBaseException(String code) {
        super(code);
    }

}
