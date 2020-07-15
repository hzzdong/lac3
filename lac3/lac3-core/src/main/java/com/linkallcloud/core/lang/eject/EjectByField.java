package com.linkallcloud.core.lang.eject;

import java.lang.reflect.Field;

import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class EjectByField implements Ejecting {

    private static final Log log = Logs.get();

    private Field field;

    public EjectByField(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    public Object eject(Object obj) {
        try {
            return null == obj ? null : field.get(obj);
        }
        catch (Exception e) {
            if (log.isInfoEnabled())
                log.info("Fail to get value by field", e);
            throw Lang.makeThrow(    "Fail to get field %s.'%s' because [%s]: %s",
                                    field.getDeclaringClass().getName(),
                                    field.getName(),
                                    Lang.unwrapThrow(e),
                                    Lang.unwrapThrow(e).getMessage());
        }
    }

}
