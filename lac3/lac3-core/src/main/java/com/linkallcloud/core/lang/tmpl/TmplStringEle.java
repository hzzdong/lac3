package com.linkallcloud.core.lang.tmpl;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.lang.Strings;

class TmplStringEle extends TmplDynamicEle {

    public TmplStringEle(String key, String fmt, String dft) {
        super(null, key, null, dft);
        this.fmt = Strings.sNull(fmt, null);
    }

    @Override
    protected String _val(Object val) {
        String re = Castors.me().castTo(val, String.class);
        if (!Strings.isBlank(this.fmt)) {
            return String.format(fmt, re);
        }
        return re;
    }

}
