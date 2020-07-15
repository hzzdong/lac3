package com.linkallcloud.core.dt;

public class DtSearch {

    private String value;// columns[0][search][value]=
    private boolean regex;// columns[0][search][regex]=false

    public DtSearch() {
        super();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

}
