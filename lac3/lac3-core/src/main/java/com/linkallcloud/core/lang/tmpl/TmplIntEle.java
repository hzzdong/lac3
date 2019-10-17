package com.linkallcloud.core.lang.tmpl;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.lang.Strings;

class TmplIntEle extends TmplDynamicEle {

    public TmplIntEle(String key, String fmt, String dft) {
        super("int", key, fmt, dft);
        this.fmt = Strings.sNull(fmt, "%d");
    }

    @Override
    protected String _val(Object val) {
        Integer n = Castors.me().castTo(val, Integer.class);
        if (null != n) {
            return String.format(fmt, n);
        }
        return null;
    }

}
