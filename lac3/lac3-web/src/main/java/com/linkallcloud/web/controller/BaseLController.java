package com.linkallcloud.web.controller;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.manager.IManager;

public abstract class BaseLController<T extends Domain, S extends IManager<T>>
        extends BaseController<T, S> {

    @SuppressWarnings("unchecked")
    public BaseLController() {
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

}
