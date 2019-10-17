package com.linkallcloud.core.el.obj;

import com.linkallcloud.core.el.ElCache;

public interface Elobj {
    public String getVal();
    public Object fetchVal();
    public void setEc(ElCache ec);
}
