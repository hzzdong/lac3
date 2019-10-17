package com.linkallcloud.web.controller;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.manager.ITreeManager;

public abstract class BaseTreeLContorller<T extends TreeDomain<Long>, S extends ITreeManager<Long, T>>
		extends BaseTreeContorller<Long, T, S> {

	@SuppressWarnings("unchecked")
	public BaseTreeLContorller() {
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
