package com.linkallcloud.fs.exception;

/**
 * 不支持的图片格式
 * 
 */
public class FdfsUnsupportImageTypeException extends FdfsException {

    private static final long serialVersionUID = 8498179372343498770L;

    public FdfsUnsupportImageTypeException(String message) {
        super(message);
    }

}
