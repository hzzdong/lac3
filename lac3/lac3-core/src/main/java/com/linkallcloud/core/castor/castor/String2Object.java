package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;

public class String2Object extends Castor<String, Object> {

    @Override
    public Object cast(String src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        if (Strings.isQuoteByIgnoreBlank(src, '{', '}'))
            return Json.fromJson(toType, src);
        return Mirror.me(toType).born(src);
    }

}
