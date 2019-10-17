package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Mirror;

public class Boolean2Number extends Castor<Boolean, Number> {

    @Override
    public Number cast(Boolean src, Class<?> toType, String... args) {
        try {
            return (Number) Mirror.me(toType)
                                    .getWrapperClass()
                                    .getConstructor(String.class)
                                    .newInstance(src ? "1" : "0");
        }
        catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

}
