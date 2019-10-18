package com.linkallcloud.web.controller;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.manager.ITreeManager;

public abstract class BaseLController4ParentTree<T extends TreeDomain, TS extends ITreeManager<T>, P extends TreeDomain, PS extends ITreeManager<P>>
        extends BaseController4ParentTree<T, TS, P, PS> {

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
