package com.linkallcloud.core.el.opt.custom;

import java.util.List;

import com.linkallcloud.core.el.opt.RunMethod;
import com.linkallcloud.core.lang.Encoding;
import com.linkallcloud.core.plugin.Plugin;
import com.linkallcloud.core.repo.Base64;

/**
 * 用法   ${base64('abc')}  ${base64('decode', 'sfasdfsadfsa')}   若传入参数为null,则返回null
 *
 */
public class DoBase64 implements RunMethod, Plugin {

    public boolean canWork() {
        return true;
    }

    public Object run(List<Object> fetchParam) {
        if (fetchParam.isEmpty())
            return null;
        if (fetchParam.size() == 1) {
            return encode(fetchParam.get(0));
        }
        Object obj = fetchParam.get(1);
        if (obj == null)
            return null;
        if ("decode".equals(fetchParam.get(0))) {
            return new String(Base64.decode(String.valueOf(obj).getBytes(Encoding.CHARSET_UTF8)), Encoding.CHARSET_UTF8);
        } else {
            return encode(obj);
        }
    }
    
    public String encode(Object obj) {
        if (obj == null)
            return null;
        return Base64.encodeToString(String.valueOf(obj).getBytes(Encoding.CHARSET_UTF8), false);
    }

    public String fetchSelf() {
        return "base64";
    }

}
