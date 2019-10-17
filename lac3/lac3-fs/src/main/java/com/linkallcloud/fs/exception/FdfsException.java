package com.linkallcloud.fs.exception;

/**
 * 封装fastdfs的异常，使用运行时异常
 * 
 */
public abstract class FdfsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected FdfsException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    protected FdfsException(String message, Throwable cause) {
        super(message, cause);
    }

}
