package com.linkallcloud.core.manager;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 服务接口基本实现（仅暴露给内部使用）
 *
 * @param <T>
 * @param <S>
 */
public abstract class BaseManager<T extends Domain, S extends IService<T>>
        implements IManager<T> {

    protected Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BaseManager() {
        super();
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    @Override
    public Class<T> getDomainClass() {
        return (null == mirror) ? null : mirror.getType();
    }

    protected abstract S service();

    @Override
    public T save(Trace t, T entity) {
        return service().save(t, entity);
    }

    @Override
    public Long insert(Trace t, T entity) {
        return service().insert(t, entity);
    }

    @Override
    public boolean update(Trace t, T entity) {
        return service().update(t, entity);
    }

    @Override
    public boolean updates(Trace t, int status, Map<String, Long> uuidIds) {
        return service().updates(t, status, uuidIds);
    }

    @Override
    public boolean updateStatus(Trace t, int status, Long id, String uuid) {
        return service().updateStatus(t, status, id, uuid);
    }

    @Override
    public boolean delete(Trace t, Long id, String uuid) {
        return service().delete(t, id, uuid);
    }

    @Override
    public boolean deletes(Trace t, Map<String, Long> uuidIdMap) {
        return service().deletes(t, uuidIdMap);
    }

    @Override
    public T fetchById(Trace t, Long id) {
        return service().fetchById(t, id);
    }

    @Override
    public T fetchByIdUuid(Trace t, Long id, String uuid) {
        return service().fetchByIdUuid(t, id, uuid);
    }

    @Override
    public List<T> findByIds(Trace t, List<Long> idList) {
        return service().findByIds(t, idList);
    }

    @Override
    public List<T> findByUuidIds(Trace t, Map<String, Long> uuidIdMap) {
        return service().findByUuidIds(t, uuidIdMap);
    }

    @Override
    public List<T> find(Trace t, Query query) {
        return service().find(t, query);
    }

    @Override
    public Page<T> findPage(Trace t, Page<T> page) {
        return service().findPage(t, page);
    }

    @Override
    public Page<T> findPage4Select(Trace t, Page<T> page) {
        return service().findPage4Select(t, page);
    }

    @Override
    public boolean exist(Trace t, T entity, String field, Object filedValue) {
        return service().exist(t, entity, field, filedValue);
    }

    @Override
    public boolean exist(Trace t, T entity, Query query) {
        return service().exist(t, entity, query);
    }

    @Override
    public <X> List<X> query(Trace t, Query query) {
        return service().query(t, query);
    }

    @Override
    public <X> Page<X> queryPage(Trace t, Page<X> page) {
        return service().queryPage(t, page);
    }
}
