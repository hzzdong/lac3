package com.linkallcloud.core.castor.castor;

import java.util.Collection;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Collection2Collection extends Castor<Collection, Collection> {

    @Override
    public Collection cast(Collection src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        Collection coll = createCollection(src, toType);
        coll.addAll(src);
        return coll;
    }

}
