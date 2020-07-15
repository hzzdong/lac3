package com.linkallcloud.core.exception;

import com.linkallcloud.core.util.ResBundle;

/**
 * 
 * 异常基类，所有异常都必须继承于此异常 . <br/>
 */
public class BaseException extends Exception {

    private static final long serialVersionUID = -5875371379845226068L;

    /**
     * 具体异常码
     */
    protected String code;

    protected BaseException() {
        this(Exceptions.CODE_ERROR_UNKNOW);
    }

    public String getCode() {
        return code;
    }

    /**
     * 实例化异常
     * 
     * @param msgFormat
     * @param args
     * @return
     */
    protected BaseException newInstance(String msgFormat, Object[] args) {
        return new BaseException(this.code, msgFormat, args);
    }

    public BaseException(String code) {
        this(code, ResBundle.getMessage(code));
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, Object[] args) {
        this(code, ResBundle.getMessage(code), args);
    }

    public BaseException(String code, String msgFormat, Object[] args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }

    public BaseException(String code, Throwable cause) {
        this(code, ResBundle.getMessage(code), cause);
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
