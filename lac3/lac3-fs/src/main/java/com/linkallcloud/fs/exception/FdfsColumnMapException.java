package com.linkallcloud.fs.exception;

/**
 * 映射例外
 * 
 */
public class FdfsColumnMapException extends RuntimeException {

    private static final long serialVersionUID = 1336200127024129847L;

    protected FdfsColumnMapException() {
    }

    protected FdfsColumnMapException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected FdfsColumnMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public FdfsColumnMapException(String message) {
        super(message);
    }

    public FdfsColumnMapException(Throwable cause) {
        super(cause);
    }

}
