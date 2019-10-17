package com.linkallcloud.core.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.exception.Exceptions;

/**
 * 通用实体类
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 119151750216961690L;

    /**
     * 返回码，默认成功=0，其它参考com.linkallcloud.exception.Exceptions
     */
    private String code;

    /**
     * 错误信息
     */
    private String message = "";

    /**
     * 返回结果实体
     */
    private T data;

    public Result() {
        super();
        this.code = Exceptions.CODE_SUCCESS;
    }

    public Result(T data) {
        super();
        this.code = Exceptions.CODE_SUCCESS;
        this.data = data;
    }

    public Result(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public Result(boolean error, String code, String message) {
        super();
        if (error) {
            this.code = code;
            this.message = message;
        } else {
            this.code = Exceptions.CODE_SUCCESS;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
