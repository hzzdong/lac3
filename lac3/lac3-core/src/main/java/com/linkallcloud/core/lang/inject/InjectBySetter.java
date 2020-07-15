package com.linkallcloud.core.lang.inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.linkallcloud.core.castor.Castors;
import com.linkallcloud.core.conf.RcConf;
import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.lang.reflect.FastClassFactory;
import com.linkallcloud.core.lang.reflect.FastMethod;

public class InjectBySetter implements Injecting {
    
    private static final Log log = Logs.get();
    
    protected FastMethod fm;
    private Method setter;
    private Class<?> valueType;
    private Type type;
    private boolean isMapCollection;

    public InjectBySetter(Method setter) {
        this.setter = setter;
        valueType = setter.getParameterTypes()[0];
        type = setter.getGenericParameterTypes()[0];
        isMapCollection = Map.class.isAssignableFrom(valueType) ||
                       Collection.class.isAssignableFrom(valueType);
    }

    public void inject(Object obj, Object value) {
        Object v = null;
        try {
            if (isMapCollection && value != null && value instanceof String) {
                v = Json.fromJson(type, value.toString());
            } else {
                v = Castors.me().castTo(value, valueType);
            }
            if (RcConf.USE_FASTCLASS) {
                if (fm == null)
                    fm = FastClassFactory.get(setter);
                fm.invoke(obj, v);
            } else {
                setter.invoke(obj, v);
            }
        }
        catch (Exception _e) {
            Throwable e = _e;
            if (e instanceof InvocationTargetException)
                e = ((InvocationTargetException)e).getTargetException();
            if (log.isInfoEnabled())
                log.info("Fail to value by setter", e);
            throw Lang.wrapThrow(e, "Fail to set '%s'[ %s ] by setter %s.'%s()' because [%s]: %s",
                                    value,
                                    v == null ? value : v,
                                    setter.getDeclaringClass().getName(),
                                    setter.getName(),
                                    Lang.unwrapThrow(e),
                                    Lang.unwrapThrow(e).getMessage());
        }
    }

}