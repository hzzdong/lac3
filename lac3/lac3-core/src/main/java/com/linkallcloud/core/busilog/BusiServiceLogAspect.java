package com.linkallcloud.core.busilog;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.JoinPoint;

import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.busilog.enums.LogMode;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.service.IService;

public abstract class BusiServiceLogAspect<T extends LacBusiLog> extends LacLogAspect<T> {

    @Override
    protected void subclassDealLogs(T operatelog, JoinPoint joinPoint, Class<?> clzz, Method method, LacLog logAnnot) {
        Mirror<?> cmirror = Mirror.me(clzz);
        operatelog.setLogMode(LogMode.SERVICE.getCode());
        if (cmirror.isOf(IService.class)) {
            
            operatelog.setOperateDesc(
                    dealStringTtemplate(false, logAnnot.desc(), joinPoint, method, new HashMap<String, Object>() {
                        private static final long serialVersionUID = 1L;
                        {
                            put("tid", operatelog.getTid());
                        }
                    }));

            String clientIP = RpcContext.getContext().getRemoteHost();
            operatelog.setIp(clientIP);

        }
    }

}
