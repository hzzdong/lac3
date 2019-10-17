package com.linkallcloud.core.castor.castor;

import java.lang.reflect.Array;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.json.JsonFormat;
import com.linkallcloud.core.lang.Lang;

public class Array2String extends Castor<Object, String> {

    public Array2String() {
        this.fromClass = Array.class;
        this.toClass = String.class;
    }

    @Override
    public String cast(Object src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        if (null != src && CharSequence.class.isAssignableFrom(src.getClass().getComponentType())) {
            return Lang.concat(",", (CharSequence[]) src).toString();
        }
        return Json.toJson(src, JsonFormat.compact());
    }

}
