package com.linkallcloud.sh;

import java.io.IOException;

public class CEStreamExhausted extends IOException {
    private static final long serialVersionUID = -2183228531481529680L;

    private String code;

    public CEStreamExhausted() {
        super();
        this.code = "CEStreamExhausted";
    }

    public CEStreamExhausted(String code, String s) {
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
