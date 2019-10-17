package com.linkallcloud.core.json.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.linkallcloud.core.json.AbstractJsonEntityFieldMaker;
import com.linkallcloud.core.json.JsonException;
import com.linkallcloud.core.json.JsonField;
import com.linkallcloud.core.json.entity.JsonEntityField;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.lang.util.Callback;
import com.linkallcloud.core.lang.util.Callback3;

/**
 * DefaultJsonEntityFieldMaker
 *
 */
public class JsonEntityFieldMakerImpl extends AbstractJsonEntityFieldMaker {

    @Override
    public JsonEntityField make(Mirror<?> mirror, Field field) {
        return JsonEntityField.eval(mirror, field);
    }

    @Override
    public JsonEntityField make(Mirror<?> mirror, final Method method) {
        final JsonField jf = method.getAnnotation(JsonField.class);
        // 忽略方法
        if (null == jf || jf.ignore())
            return null;
        final JsonEntityField[] result = new JsonEntityField[1];
        // 如果有，尝试作新的 Entity
        Callback<Method> whenError = new Callback<Method>() {
            // 给定方法即不是 getter 也不是 setter，靠！玩我!
            public void invoke(Method m) {
                throw Lang.makeThrow(JsonException.class,
                                     "JsonField '%s' should be getter/setter pair!",
                                     m);
            }
        };
        Callback3<String, Method, Method> whenOk = new Callback3<String, Method, Method>() {
            public void invoke(String name, Method getter, Method setter) {
                // 防止错误
                if (null == getter || null == setter || Strings.isBlank(name)) {
                    throw Lang.makeThrow(JsonException.class,
                                         "JsonField '%s' should be getter/setter pair!",
                                         method);
                }
                // 加入字段表
                JsonEntityField ef = JsonEntityField.eval(Strings.sBlank(jf.value(), name),
                                                          getter,
                                                          setter);
                result[0] = ef;
            }
        };
        Mirror.evalGetterSetter(method, whenOk, whenError);
        return result[0];
    }

}