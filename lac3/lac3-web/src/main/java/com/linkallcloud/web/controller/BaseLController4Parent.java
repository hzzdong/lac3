package com.linkallcloud.web.controller;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.manager.IManager;

public abstract class BaseLController4Parent<T extends TreeDomain, TS extends IManager<T>, P extends Domain, PS extends IManager<P>>
        extends BaseController4Parent<T, TS, P, PS> {

    @SuppressWarnings("unchecked")
    public BaseLController4Parent() {
        super();
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

}
