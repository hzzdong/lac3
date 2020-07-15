package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.lang.Mirror;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Class2Mirror extends Castor<Class, Mirror> {

    @Override
    public Mirror<?> cast(Class src, Class toType, String... args) {
        return Mirror.me(src);
    }

}
