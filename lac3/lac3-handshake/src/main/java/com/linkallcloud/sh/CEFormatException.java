package com.linkallcloud.sh;

import java.io.IOException;

public class CEFormatException extends IOException {
    private static final long serialVersionUID = -881931257255842159L;

    private String code;

    public CEFormatException(String code, String s) {
        super(s);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
