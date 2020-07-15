package com.linkallcloud.web.face.annotation;

import com.linkallcloud.core.enums.EnumBase;

public enum FdMode implements EnumBase<Integer> {
    ORIGINAL(0, "原始模式，InputStrim转String作为Controller参数"), JSON(1, "json模式：InputStrim转json"),
    XML(2, "xml模式：InputStrim转xml");

    private Integer code;
    private String message;

    FdMode(Integer code, String message) {
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

    public static FdMode get(Integer code) {
        for (FdMode ul : values()) {
            if (ul.getCode().equals(code)) {
                return ul;
            }
        }
        return null;
    }

}
