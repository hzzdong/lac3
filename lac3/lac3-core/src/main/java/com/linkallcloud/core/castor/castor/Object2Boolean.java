package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;

public class Object2Boolean extends Castor<Object, Boolean> {

    @Override
    public Boolean cast(Object src, Class<?> toType, String... args) {
        if (null == src)
            return Boolean.FALSE;

        return true;
    }

}
