package com.linkallcloud.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.activity.ILacLogActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;

public abstract class LacLogService<T extends BusiLog, A extends ILacLogActivity<T>> implements ILacLogService<T> {
	protected Log log = Logs.get();

	public abstract A activity();

	@Transactional(readOnly = false)
	@Override
	public Long insert(Trace t, T entity) {
		if (entity.getErrorMessage() != null && entity.getErrorMessage().length() > 512) {
			entity.setErrorMessage(entity.getErrorMessage().substring(0, 512));
		}
		return activity().insert(t, entity);
	}

	@Transactional(readOnly = false)
	@Override
	public boolean delete(Trace t, Long id, String uuid) {
		return activity().delete(t, id, uuid);
	}

	@Override
	public T fetchById(Trace t, Long id) {
		return activity().fetchById(t, id);
	}

	@Override
	public List<T> find(Trace t, Query query) {
		return activity().find(t, query);
	}

	@Override
	public Page<T> findPage(Trace t, Page<T> page) {
		return activity().findPage(t, page);
	}

}
