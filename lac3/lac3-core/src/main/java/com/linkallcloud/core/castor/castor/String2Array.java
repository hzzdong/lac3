package com.linkallcloud.core.castor.castor;

import java.lang.reflect.Array;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;

public class String2Array extends Castor<String, Object> {

    public String2Array() {
        this.fromClass = String.class;
        this.toClass = Array.class;
    }

    @Override
    public Object cast(String src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        if (Strings.isQuoteByIgnoreBlank(src, '[', ']')) {
            return Json.fromJson(toType, src);
        }
        String[] ss = Strings.splitIgnoreBlank(src);
        return Lang.array2array(ss, toType.getComponentType());
    }

}
