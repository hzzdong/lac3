package com.linkallcloud.core.dto.born;

import java.io.Serializable;

import com.linkallcloud.core.domain.IDomain;
import com.linkallcloud.core.dto.FaceDto;
import com.linkallcloud.core.lang.Mirror;

/**
 * 默认构造方式
 * 
 */
public class DefaultDtoBorning implements DtoBorning {
    private Mirror<?> mirror;

    public DefaultDtoBorning(Mirror<?> mirror) {
        this.mirror = mirror;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object born(Object[] args) throws Exception {
        Object result = mirror.born();
        IDomain entity = matchArgs(args);
        if (result instanceof FaceDto) {
            ((FaceDto) result).fromDomain(entity);
        }
        return result;
    }

    /**
     * 检查参数中是否匹配到参数Entity
     * 
     * @param args
     * @return
     */
    @SuppressWarnings("unchecked")
    private IDomain<Serializable> matchArgs(Object[] args) {
        if (null == args || args.length <= 0) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof IDomain) {
                return (IDomain<Serializable>) arg;
            }
        }
        return null;
    }

}
