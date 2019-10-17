package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shadow implements Serializable {
    private static final long serialVersionUID = 771982781627593524L;

    private List<String> keys;

    public Shadow() {
        super();
    }

    public Shadow(List<String> keys) {
        super();
        this.keys = keys;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public String[] keys() {
        if (this.keys == null || keys.isEmpty()) {
            return new String[0];
        }
        return this.keys.toArray(new String[this.keys.size()]);
    }

    public List<String> addKey(String key) {
        if (this.keys == null || keys.isEmpty()) {
            this.keys = new ArrayList<>();
        }
        this.keys.add(key);
        return this.keys;
    }

    public List<String> addKeys(List<String> addKeys) {
        if (this.keys == null || keys.isEmpty()) {
            this.keys = new ArrayList<>();
        }
        this.keys.addAll(addKeys);
        return this.keys;
    }

    public List<String> removeKey(String key) {
        if (this.keys != null && !keys.isEmpty()) {
            if (this.keys.contains(key)) {
                this.keys.remove(key);
            }
        }
        return this.keys;
    }

}
