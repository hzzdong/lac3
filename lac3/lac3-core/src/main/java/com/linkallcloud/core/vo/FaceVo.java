package com.linkallcloud.core.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class FaceVo extends Vo {
    private static final long serialVersionUID = -2144972348935799543L;

    protected static Log log = Logs.get();

    @JSONField(serialize = false)
    protected int versn; // 版本号

    public FaceVo() {
        super();
    }

    public FaceVo(Object o) {
        super(o);
    }

    public int getVersn() {
        return versn;
    }

    public void setVersn(int versn) {
        this.versn = versn;
    }

}
