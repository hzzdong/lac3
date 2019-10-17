package com.linkallcloud.core.castor.castor;

import java.lang.reflect.Method;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.json.JsonFormat;
import com.linkallcloud.core.lang.Mirror;

public class Object2String extends Castor<Object, String> {

    @Override
    public String cast(Object src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        for (Method method : Mirror.me(src).getMethods()) {
            if ("toString".equals(method.getName())) {
                return src.toString();
            }
        }
        return Json.toJson(src, JsonFormat.tidy());
    }

}
