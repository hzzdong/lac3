package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;

public class String2Boolean extends Castor<String, Boolean> {

    @Override
    public Boolean cast(String src, Class<?> toType, String... args) {
        if (Strings.isBlank(src))
            return false;
        return Lang.parseBoolean(src);
    }

}
