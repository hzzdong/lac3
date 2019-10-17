package com.linkallcloud.web.controller;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.manager.ITreeManager;

public abstract class BaseLController4ParentTree<T extends TreeDomain<Long>, TS extends ITreeManager<Long, T>, P extends TreeDomain<Long>, PS extends ITreeManager<Long, P>>
        extends BaseController4ParentTree<Long, T, TS, P, PS> {

    @SuppressWarnings("unchecked")
    public BaseLController4ParentTree() {
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
