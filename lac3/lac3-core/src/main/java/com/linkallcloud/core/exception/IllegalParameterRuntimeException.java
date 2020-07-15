package com.linkallcloud.core.exception;

import com.linkallcloud.core.util.ResBundle;

public class IllegalParameterRuntimeException extends BaseRuntimeException {
    private static final long serialVersionUID = 9064029361633903337L;

    public IllegalParameterRuntimeException() {
        super(Exceptions.CODE_ERROR_PARAMETER, ResBundle.getMessage(Exceptions.CODE_ERROR_PARAMETER));
    }

    public IllegalParameterRuntimeException(Throwable cause) {
        super(Exceptions.CODE_ERROR_PARAMETER, ResBundle.getMessage(Exceptions.CODE_ERROR_PARAMETER), cause);
    }

    public IllegalParameterRuntimeException(String code, Object[] args) {
        super(code, args);
    }

    public IllegalParameterRuntimeException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public IllegalParameterRuntimeException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public IllegalParameterRuntimeException(String code, String message) {
        super(code, message);
    }

    public IllegalParameterRuntimeException(String code, Throwable cause) {
        super(code, cause);
    }

    public IllegalParameterRuntimeException(String code) {
        super(code);
    }

}
