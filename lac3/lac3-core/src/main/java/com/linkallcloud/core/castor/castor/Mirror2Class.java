package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class Mirror2Class extends Castor<Mirror, Class> {

    @Override
    public Class cast(Mirror src, Class toType, String... args) {
        return src.getType();
    }

}
