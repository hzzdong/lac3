package com.linkallcloud.core.dto;

import java.util.List;
import java.util.UUID;

public class Trace extends Shadow {
    private static final long serialVersionUID = -5644905014839200215L;

    private String tid;

    public Trace() {
        super();
    }

    public Trace(boolean autoGenerateTid) {
        super();
        if (autoGenerateTid) {
            this.tid = UUID.randomUUID().toString().replace("-", "");
        }
    }

    public Trace(List<String> keys) {
        super(keys);
    }

    public Trace(String tid) {
        super();
        this.tid = tid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    // public String getTidIfNullThenGenerate() {
    // if (Strings.isBlank(this.tid)) {
    // this.tid = UUID.randomUUID().toString().replace("-", "");
    // }
    // return tid;
    // }

}
