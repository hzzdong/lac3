package com.linkallcloud.core.manager;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.service.ILacLogService;

public abstract class LacLogManager<T extends BusiLog, S extends ILacLogService<T>> implements ILacLogManager<T> {

	protected abstract S service();

	@Override
	public Long insert(Trace t, T entity) {
		return service().insert(t, entity);
	}

	@Override
	public boolean delete(Trace t, Long id, String uuid) {
		return service().delete(t, id, uuid);
	}

	@Override
	public T fetchById(Trace t, Long id) {
		return service().fetchById(t, id);
	}

	@Override
	public List<T> find(Trace t, Query query) {
		return service().find(t, query);
	}

	@Override
	public Page<T> findPage(Trace t, Page<T> page) {
		return service().findPage(t, page);
	}

}
