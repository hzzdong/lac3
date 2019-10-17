package com.linkallcloud.core.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.service.IService;

/**
 * 服务接口基本实现（仅暴露给内部使用）
 *
 * @param <PK>
 * @param <T>
 * @param <S>
 */
public abstract class BaseManager<PK extends Serializable, T extends Domain<PK>, S extends IService<PK, T>>
        implements IManager<PK, T> {

    protected abstract S service();

    @Override
    public PK insert(Trace t, T entity) {
        return service().insert(t, entity);
    }

    @Override
    public boolean update(Trace t, T entity) {
        return service().update(t, entity);
    }

    @Override
    public boolean updates(Trace t, int status, Map<String, PK> uuidIds) {
        return service().updates(t, status, uuidIds);
    }

    @Override
    public boolean updateStatus(Trace t, int status, PK id, String uuid) {
        return service().updateStatus(t, status, id, uuid);
    }

    @Override
    public boolean delete(Trace t, PK id, String uuid) {
        return service().delete(t, id, uuid);
    }

    @Override
    public boolean deletes(Trace t, Map<String, PK> uuidIdMap) {
        return service().deletes(t, uuidIdMap);
    }

    @Override
    public T fetchById(Trace t, PK id) {
        return service().fetchById(t, id);
    }

    @Override
    public T fetchByIdUuid(Trace t, PK id, String uuid) {
        return service().fetchByIdUuid(t, id, uuid);
    }

    @Override
    public List<T> findByIds(Trace t, List<PK> idList) {
        return service().findByIds(t, idList);
    }

    @Override
    public List<T> findByUuidIds(Trace t, Map<String, PK> uuidIdMap) {
        return service().findByUuidIds(t, uuidIdMap);
    }

    @Override
    public List<T> find(Trace t, Query query) {
        return service().find(t, query);
    }

    @Override
    public Page<PK, T> findPage(Trace t, Page<PK, T> page) {
        return service().findPage(t, page);
    }

    @Override
    public Page<PK, T> findPage4Select(Trace t, Page<PK, T> page) {
        return service().findPage4Select(t, page);
    }

}
