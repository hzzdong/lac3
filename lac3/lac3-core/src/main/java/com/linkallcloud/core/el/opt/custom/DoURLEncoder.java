package com.linkallcloud.core.el.opt.custom;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.linkallcloud.core.el.opt.RunMethod;
import com.linkallcloud.core.lang.Encoding;
import com.linkallcloud.core.plugin.Plugin;

public class DoURLEncoder implements RunMethod, Plugin  {

    public boolean canWork() {
        return true;
    }

    public Object run(List<Object> fetchParam) {
        if (fetchParam.isEmpty())
            throw new IllegalArgumentException("need args!!");
        Object val = fetchParam.get(0);
        if (val == null)
            return "";
        Object enc = null;
        if (fetchParam.size() > 1) {
            enc = fetchParam.get(1);
        }
        if (enc == null)
            enc = Encoding.UTF8;
        try {
            return URLEncoder.encode(val.toString(), enc.toString());
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("can't do urlencode[" + val + "]", e);
        }
    }

    public String fetchSelf() {
        return "urlencode";
    }

}
