package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Picker implements Serializable {
    private static final long serialVersionUID = 7435395711272122607L;

    private String id;
    private String value;
    private String recommend;
    private List<Picker> childs;

    private String code;
    private String name;

    public Picker() {
    }

    public Picker(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public Picker(String id, String value, String recommend) {
        this.id = id;
        this.value = value;
        this.recommend = recommend;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public List<Picker> getChilds() {
        return childs;
    }

    public void addChild(Picker child) {
        if (childs == null) {
            childs = new ArrayList<>();
        }
        childs.add(child);
    }

    public void setChilds(List<Picker> childs) {
        this.childs = childs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
