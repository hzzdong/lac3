package com.linkallcloud.core.busilog.enums;

public enum LogMode implements EnumBase<Integer> {
    WEB(0, "WEB层日志"), SERVICE(1, "服务层日志"), FILE(2, "日志文件");

    private int code;
    private String message;

    LogMode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
    public static LogMode get(Integer code) {
        for (LogMode ul : values()) {
            if (ul.getCode().equals(code)) {
                return ul;
            }
        }
        return null;
    }

}
