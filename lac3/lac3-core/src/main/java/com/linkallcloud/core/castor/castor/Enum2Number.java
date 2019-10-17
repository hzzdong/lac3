package com.linkallcloud.core.castor.castor;

import com.linkallcloud.core.castor.Castor;
import com.linkallcloud.core.castor.FailToCastObjectException;
import com.linkallcloud.core.lang.Mirror;

@SuppressWarnings({"rawtypes"})
public class Enum2Number extends Castor<Enum, Number> {

    @Override
    public Number cast(Enum src, Class<?> toType, String... args)
            throws FailToCastObjectException {
        Mirror<?> mi = Mirror.me(src);

        // 首先尝试调用枚举对象的 value() 方法
        try {
            return (Number) mi.invoke(src, "value");
        }
        // 如果失败，就用其顺序号
        catch (Exception e) {
            Integer re = src.ordinal();
            return (Number) Mirror.me(toType).born(re.toString());
        }
    }

}
