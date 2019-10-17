package com.linkallcloud.core.aop.asm;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import com.linkallcloud.core.aop.AbstractClassAgent;
import com.linkallcloud.core.aop.ClassDefiner;
import com.linkallcloud.core.aop.MethodInterceptor;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.repo.org.objectweb.asm.Opcodes;

public class AsmClassAgent extends AbstractClassAgent {

    static int CLASS_LEVEL = Opcodes.V1_5;
    
    static final String MethodArray_FieldName = "_$$Nut_methodArray";
    static final String MethodInterceptorList_FieldName = "_$$Nut_methodInterceptorList";

    static {
        if (Lang.isJDK6())
            CLASS_LEVEL = Opcodes.V1_6;
        Logs.get().debugf("AsmClassAgent will define class in Version %s",CLASS_LEVEL);
    }

    @SuppressWarnings("unchecked")
    protected <T> Class<T> generate(ClassDefiner cd,
                                    Pair2[] pair2s,
                                    String newName,
                                    Class<T> klass,
                                    Constructor<T>[] constructors) {
        Method[] methodArray = new Method[pair2s.length];
        List<MethodInterceptor>[] methodInterceptorList = new List[pair2s.length];
        for (int i = 0; i < pair2s.length; i++) {
            Pair2 pair2 = pair2s[i];
            methodArray[i] = pair2.getMethod();
            methodInterceptorList[i] = pair2.getListeners();
        }
        byte[] bytes = ClassY.enhandClass(klass, newName, methodArray, constructors);
        Class<T> newClass = (Class<T>) cd.define(newName, bytes, klass.getClassLoader());
        try {
            Mirror<T> mirror = Mirror.me(newClass);
            mirror.setValue(null, MethodArray_FieldName, methodArray);
            mirror.setValue(null, MethodInterceptorList_FieldName, methodInterceptorList);
        }
        catch (Throwable e) {}
        return newClass;
    }

}
