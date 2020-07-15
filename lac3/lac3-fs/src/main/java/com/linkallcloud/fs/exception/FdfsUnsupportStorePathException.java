package com.linkallcloud.fs.exception;

/**
 * 从Url解析StorePath文件路径对象错误
 * 
 */
public class FdfsUnsupportStorePathException extends FdfsException {
    private static final long serialVersionUID = 8116336411011152869L;

    public FdfsUnsupportStorePathException(String message) {
        super(message);
    }

}
