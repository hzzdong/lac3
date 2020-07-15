package com.linkallcloud.core.json.entity;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.linkallcloud.core.json.Json;
import com.linkallcloud.core.json.JsonEntityFieldMaker;
import com.linkallcloud.core.json.JsonFormat;
import com.linkallcloud.core.json.ToJson;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.lang.born.Borning;
import com.linkallcloud.core.lang.born.BorningException;

/**
 * 记录一个Java如何映射 JSON 字符串的规则
 * 
 */
public class JsonEntity {

    private List<JsonEntityField> fields;

    private Map<String, JsonEntityField> fieldMap = new LinkedHashMap<String, JsonEntityField>();

    private Borning<?> borning;

    private BorningException err;

    private Map<String, Integer> typeParams; // 如果本类型是范型，存放范型标识的下标
    
    private Method toJsonMethod;
    
    private JsonEntityFieldMaker fieldMaker;

    public JsonEntity(Mirror<?> mirror) {
        fieldMaker = Json.getDefaultFieldMaker();
        // 处理范型
        Type type = mirror.getActuallyType();
        typeParams = new LinkedHashMap<String, Integer>();
        if (type instanceof ParameterizedType) {
            ParameterizedType pmType = (ParameterizedType) type;
            int i = 0;
            for (Type pmA : pmType.getActualTypeArguments()) {
                typeParams.put(pmA.toString(), i++);
            }
        }
        // 开始解析
        fields = fieldMaker.make(mirror);
        for (JsonEntityField ef : fields)
            fieldMap.put(ef.getName(), ef);

        try {
            borning = mirror.getBorning();
        }
        catch (BorningException e) {
            err = e;
        }
        
        Class<? extends Object> klass = mirror.getType();
        ToJson tj = klass.getAnnotation(ToJson.class);
        String myMethodName = Strings.sNull(null == tj ? null : tj.value(), "toJson");
        try {
            /*
             * toJson()
             */
            try {
                Method myMethod = klass.getMethod(myMethodName);
                if (!myMethod.isAccessible())
                    myMethod.setAccessible(true);
                toJsonMethod = myMethod;
            }
            /*
             * toJson(JsonFormat fmt)
             */
            catch (NoSuchMethodException e1) {
                try {
                    Method myMethod = klass.getMethod(myMethodName, JsonFormat.class);
                    if (!myMethod.isAccessible())
                        myMethod.setAccessible(true);
                    toJsonMethod = myMethod;
                }
                catch (NoSuchMethodException e) {}
            }
        }
        catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }

    public List<JsonEntityField> getFields() {
        return fields;
    }

    public Object born() {
        if (null == borning)
            throw err;
        return borning.born(new Object[0]);
    }

    public JsonEntityField getField(String name) {
        return fieldMap.get(name);
    }
    
    public Method getToJsonMethod() {
        return toJsonMethod;
    }

}