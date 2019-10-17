package com.linkallcloud.core.lang.tmpl;

import java.util.Date;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.lang.Times;

class TmplDateEle extends TmplDynamicEle {

    public TmplDateEle(String key, String fmt, String dft) {
        super("date", key, fmt, dft);
        this.fmt = Strings.sNull(fmt, "yyyy-MM-dd'T'HH:mm:ss");
    }

    @Override
    protected String _val(Object val) {
        Date d = Castors.me().castTo(val, Date.class);
        if (null != d)
            return Times.format(fmt, d);
        return null;
    }

}
