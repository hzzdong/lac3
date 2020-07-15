package com.linkallcloud.core.dto.born;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.linkallcloud.core.lang.MatchType;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;


/**
 * DTO构造工厂搜索工具
 * 
 */
public class DtoBorns {
    private static Map<String, DtoBorning> dtoBorningMap = new HashMap<String, DtoBorning>();
    private static final DtoBorning NO_BORNING = new DefaultDtoBorning(null);

    /**
     * 搜索DTO构造工厂
     * 
     * 搜索的步骤如下：1、 若methodName非空，优先搜索参数完全匹配的静态方法；2、 若找不到，搜索参数完全匹配的构造器；3、 若还是找不到，继续找参数完全匹配的其他方法；4、 若还是找不到，返回默认的DTO构造工厂。
     * 
     * @param classOfDTO
     * @param entity
     * @param methodName
     * @param createArgs
     * @return
     */
    public static DtoBorning evalBorning(Class<?> classOfDTO, Object entity, String methodName,
            Class<?>[] createArgTypes) {
        String key = getDtoBorningKey(classOfDTO, methodName, createArgTypes);
        DtoBorning borning = dtoBorningMap.get(key);
        if (null != borning) {
            return NO_BORNING == borning ? null : borning;
        }

        Method method = null;
        if (!Strings.isBlank(methodName)) {
            try {
                method = classOfDTO.getMethod(methodName, createArgTypes);
            } catch (Throwable e) {
            }
            if (null != method) {
                borning = new StaticMethodBorning(method);
            }
        }

        if (null == borning) {
            Constructor<?> constructor = evalDtoBornConstructor(classOfDTO, createArgTypes);
            if (null != constructor) {
                borning = new ConstructorBorning(constructor);
            }
        }

        if (null == borning) {
            method = evalDtoBornMethod(classOfDTO, createArgTypes);
            if (null != method) {
                borning = new StaticMethodBorning(method);
            }
        }
        if (null == borning) {
            try {
                borning = new DefaultDtoBorning(Mirror.me(classOfDTO));
            } catch (Throwable e1) {
                // throw Lang.makeThrow("Dto [%s] is invailid.",
                // classOfDTO.getName());
            }
        }

        dtoBorningMap.put(key, null == borning ? NO_BORNING : borning);
        return borning;
    }

    /**
     * 得到DtoBorning的key
     * 
     * @param classOfDTO
     * @param methodName
     * @param createArgTypes
     * @return key
     */
    private static String getDtoBorningKey(Class<?> classOfDTO, String methodName, Class<?>[] createArgTypes) {
        StringBuffer keyBuffer = new StringBuffer();
        keyBuffer.append(classOfDTO.getName()).append("-").append(methodName);
        for (Class<?> clas : createArgTypes) {
            keyBuffer.append("-").append(clas.getName());
        }
        return keyBuffer.toString();
    }

    /**
     * 得到DTO中参数符合的静态构造方法
     * 
     * @param classOfDTO
     *            DTO类型
     * @param methodArgTypes
     *            构造方法参数类型
     * @return 静态构造方法
     */
    private static Method evalDtoBornMethod(Class<?> classOfDTO, Class<?>[] methodArgTypes) {
        if (null == classOfDTO || null == methodArgTypes) {
            return null;
        }

        Method[] methods = classOfDTO.getMethods();
        if (null != methods && methods.length > 0) {
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                    Class<?> returnTrype = method.getReturnType();
                    if (returnTrype == classOfDTO || returnTrype.equals(classOfDTO)
                            || returnTrype.isAssignableFrom(classOfDTO)) {
                        MatchType mt = Mirror.matchParamTypes(method.getParameterTypes(), methodArgTypes);
                        if (MatchType.YES == mt) {
                            return method;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 得到DTO中参数符合的构造器
     * 
     * @param classOfDTO
     *            DTO类型
     * @param constructorArgTypes
     *            构造器参数类型
     * @return Entity构造器
     */
    private static Constructor<?> evalDtoBornConstructor(Class<?> classOfDTO, Class<?>[] constructorArgTypes) {
        if (null == classOfDTO || null == constructorArgTypes) {
            return null;
        }

        try {
            for (Constructor<?> cc : classOfDTO.getConstructors()) {
                MatchType mt = Mirror.matchParamTypes(cc.getParameterTypes(), constructorArgTypes);
                if (MatchType.YES == mt) {
                    return cc;
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

}
