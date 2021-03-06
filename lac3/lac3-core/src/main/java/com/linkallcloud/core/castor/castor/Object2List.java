package com.linkallcloud.core.castor.castor;

import java.util.ArrayList;
import java.util.List;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Lang;

@SuppressWarnings({"rawtypes"})
public class Object2List extends Castor<Object, List> {

    @Override
    @SuppressWarnings("unchecked")
    public List cast(Object src, Class<?> toType, String... args) throws FailToCastObjectException {
        try {
            List<Object> list = (List<Object>) (toType == List.class ? new ArrayList<Object>(1)
                                                                    : toType.getDeclaredConstructor().newInstance());
            list.add(src);
            return list;
        }
        catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

}
