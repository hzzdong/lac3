package com.linkallcloud.core.dto;

import java.io.Serializable;

public class Sid implements Serializable {
    private static final long serialVersionUID = 2050856804575088020L;

    private Long id;
    private String uuid;
    private String code;
    private String name;

    public Sid() {
        super();
    }

    public Sid(Long id, String uuid) {
        this();
        this.id = id;
        this.uuid = uuid;
    }

    public Sid(Long id, String uuid, String name) {
        this(id, uuid);
        this.name = name;
    }

    public Sid(Long id, String uuid, String code, String name) {
        this(id, uuid, name);
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
