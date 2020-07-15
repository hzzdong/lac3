package com.linkallcloud.core.lang.tmpl;

import com.linkallcloud.core.lang.util.RcBean;

class TmplStaticEle implements TmplEle {

    private String str;

    public TmplStaticEle(String str) {
        this.str = str;
    }

    @Override
    public void join(StringBuilder sb, RcBean context, boolean showKey) {
        sb.append(str);
    }

    @Override
    public String toString() {
        return str.replace("$", "$$");
    }

}
