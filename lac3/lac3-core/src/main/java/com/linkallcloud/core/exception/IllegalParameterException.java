package com.linkallcloud.core.exception;

import com.linkallcloud.core.util.ResBundle;

public class IllegalParameterException extends BaseException {
    private static final long serialVersionUID = 4734450290170172472L;

    public IllegalParameterException() {
        super(Exceptions.CODE_ERROR_PARAMETER, ResBundle.getMessage(Exceptions.CODE_ERROR_PARAMETER));
    }

    public IllegalParameterException(String code, Object[] args) {
        super(code, args);
    }

    public IllegalParameterException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public IllegalParameterException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public IllegalParameterException(String code, String message) {
        super(code, message);
    }

    public IllegalParameterException(String code, Throwable cause) {
        super(code, cause);
    }

    public IllegalParameterException(String code) {
        super(code);
    }

    public IllegalParameterException(Throwable cause) {
        super(Exceptions.CODE_ERROR_PARAMETER, ResBundle.getMessage(Exceptions.CODE_ERROR_PARAMETER), cause);
    }

}
