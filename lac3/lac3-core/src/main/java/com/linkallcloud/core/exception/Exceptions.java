package com.linkallcloud.core.exception;

import java.util.HashMap;
import java.util.Map;

import com.linkallcloud.core.dto.Result;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

public class Exceptions {

    public static final String CODE_SUCCESS = "0";// 成功

    public static final String CODE_ERROR_UNKNOW = "9001";// 未知错误
    public static final String CODE_ERROR_PARAMETER = "9002";// 参数错误

    public static final String CODE_ERROR_AUTH_ACCOUNT = "9003";// 账号验证失败
    public static final String CODE_ERROR_AUTH_PERMISSION = "9004";// 无权限

    public static final String CODE_ERROR_DB = "9005";// 数据库异常
    public static final String CODE_ERROR_CACHE = "9006";// 缓存异常
    public static final String CODE_ERROR_FILE = "9007";// 文件错误
    public static final String CODE_ERROR_IMAGE = "9008";// 图片错误
    
    public static final String CODE_ERROR_DELETE = "9010";// DEL错误
    public static final String CODE_ERROR_ADD = "9011";// 新增错误
    public static final String CODE_ERROR_UPDATE = "9012";// 更新错误
    public static final String CODE_ERROR_SEARCH = "9013";// 查询错误
    public static final String CODE_ERROR_SYNC = "9014";// 同步错误
    
    public static final String CODE_ERROR_TIMEOUT = "9020";// 超时错误
    public static final String CODE_ERROR_SESSION_TIMEOUT = "9021";// 会话超时错误

    public static Result<Object> makeErrorResult(Throwable t) {
        Result<Object> result = new Result<>(Exceptions.CODE_ERROR_UNKNOW, "未知错误，请联系管理员。");
        if (t != null) {
            if (t.getClass().equals(BaseException.class)) {
                BaseException be = (BaseException) t;
                result.setCode(be.getCode());
                result.setMessage(be.getMessage());
            } else if (t.getClass().equals(BaseRuntimeException.class)) {
                BaseRuntimeException be = (BaseRuntimeException) t;
                result.setCode(be.getCode());
                result.setMessage(be.getMessage());
            } else if (t instanceof MaxUploadSizeExceededException) {
                result.setCode(Exceptions.CODE_ERROR_FILE);
                result.setMessage("文件太大啦！请选择更小一些的文件重试！");
            }
        }
        return result;
    }

    public static Result<Object> makeErrorResult(String code, String message) {
        return new Result<Object>(code, message);
    }

    public static Map<String, Object> makeErrorMap(Throwable t) {
        Map<String, Object> result = new HashMap<>();
        if (t == null) {
            result.put("code", Exceptions.CODE_ERROR_UNKNOW);
            result.put("message", "未知错误，请联系管理员。");
        } else {
            if (t.getClass().equals(BaseException.class)) {
                BaseException be = (BaseException) t;
                result.put("code", be.getCode());
                result.put("message", be.getMessage());
            } else if (t.getClass().equals(BaseRuntimeException.class)) {
                BaseRuntimeException be = (BaseRuntimeException) t;
                result.put("code", be.getCode());
                result.put("message", be.getMessage());
            } else if (t instanceof MaxUploadSizeExceededException) {
                result.put("code", Exceptions.CODE_ERROR_FILE);
                result.put("message", "文件太大啦！请选择更小一些的文件重试！");
            } else {
                result.put("code", Exceptions.CODE_ERROR_UNKNOW);
                result.put("message", t.getMessage());
            }
        }
        return result;
    }

    public static Map<String, Object> makeErrorMap(String code, String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", code);
        result.put("message", message);
        return result;
    }

}
