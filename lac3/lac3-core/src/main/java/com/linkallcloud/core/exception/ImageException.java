package com.linkallcloud.core.exception;

import com.linkallcloud.core.util.ResBundle;

public class ImageException extends BaseException {
    private static final long serialVersionUID = 1869312432423502279L;

    
    public ImageException() {
        this(Exceptions.CODE_ERROR_IMAGE);
    }

    public ImageException(String code) {
        this(code, ResBundle.getMessage(code));
    }

    public ImageException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ImageException(String code, Object[] args) {
        super(code, args);
    }

    public ImageException(String code, String msgFormat, Object[] args) {
        super(code, msgFormat, args);
    }

    public ImageException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ImageException(String code, Throwable cause) {
        super(code, cause);
    }
    

}
