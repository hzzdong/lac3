package com.linkallcloud.core.castor.castor;

import java.util.Map;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;

@SuppressWarnings({"rawtypes"})
public class String2Map extends Castor<String, Map> {

    @Override
    public Map cast(String src, Class<?> toType, String... args) throws FailToCastObjectException {
        return (Map) Json.fromJson(toType, src);
    }

}
