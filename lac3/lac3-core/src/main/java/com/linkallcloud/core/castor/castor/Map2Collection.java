package com.linkallcloud.core.castor.castor;

import java.util.Collection;
import java.util.Map;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Map2Collection extends Castor<Map, Collection> {

    @Override
    public Collection cast(Map src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        Collection coll = createCollection(src, toType);
        coll.add(src);
        return coll;
    }

}
