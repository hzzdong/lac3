package com.linkallcloud.sh.sm;

public class MsgException extends Exception {
    private static final long serialVersionUID = 4979347355731259080L;
    private String code;

    public MsgException(String code, String s) {
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
