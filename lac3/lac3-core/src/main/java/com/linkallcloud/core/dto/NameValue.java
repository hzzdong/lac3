package com.linkallcloud.core.dto;

import java.io.Serializable;

public class NameValue implements Serializable {

    private String key;
    private String value;
    private String title;

    public NameValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public NameValue(String key, String value, String title) {
        this.key = key;
        this.value = value;
        this.title = title;
    }

    public String toString() {
        return key + "=" + value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
