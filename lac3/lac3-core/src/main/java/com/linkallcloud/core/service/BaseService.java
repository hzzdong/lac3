package com.linkallcloud.core.service;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public abstract class BaseService<T extends Domain, A extends IActivity<T>> implements IService<T> {
    protected Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BaseService() {
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

    public abstract A activity();

    @Override
    public boolean exist(Trace t, T entity, String field, Object filedValue) {
        return activity().exist(t, entity, field, filedValue);
    }

    @Override
    public boolean exist(Trace t, T entity, Query query) {
        return activity().exist(t, entity, query);
    }

    @LacLog()
    @Transactional(readOnly = false)
    @Override
    public T save(Trace t, T entity) {
        if (entity.getId() == null) {
            Long id = insert(t, entity);
            entity.setId(id);
        } else {
            update(t, entity);
        }
        return entity;
    }

    @LacLog()
    @Transactional(readOnly = false)
    @Override
    public Long insert(Trace t, T entity) {
        return activity().insert(t, entity);
    }

    @LacLog()
    @Transactional(readOnly = false)
    @Override
    public boolean update(Trace t, T entity) {
        return activity().update(t, entity);
    }

    @LacLog(desc = "修改 [(${domainShowName})]([(${uuidIds})])的状态为([(${status})]), TID:[(${t.tid})]")
    @Transactional(readOnly = false)
    @Override
    public boolean updates(Trace t, int status, Map<String, Long> uuidIds) {
        return activity().updates(t, status, uuidIds);
    }

    @LacLog(desc = "修改 [(${domainShowName})]([(${id})])的状态为([(${status})]), TID:[(${t.tid})]")
    @Transactional(readOnly = false)
    @Override
    public boolean updateStatus(Trace t, int status, Long id, String uuid) {
        return activity().updateStatus(t, status, id, uuid);
    }

    @LacLog(desc = "删除 [(${domainShowName})]([(${id})]), TID:[(${t.tid})]")
    @Transactional(readOnly = false)
    @Override
    public boolean delete(Trace t, Long id, String uuid) {
        return activity().delete(t, id, uuid);
    }

    @LacLog(desc = "删除 [(${domainShowName})]([(${uuidIds})]), TID:[(${t.tid})]")
    @Transactional(readOnly = false)
    @Override
    public boolean deletes(Trace t, Map<String, Long> uuidIdMap) {
        return activity().deletes(t, uuidIdMap);
    }

    @Override
    public T fetchById(Trace t, Long id) {
        return activity().fetchById(t, id);
    }

    @Override
    public T fetchByIdUuid(Trace t, Long id, String uuid) {
        return activity().fetchByIdUuid(t, id, uuid);
    }

    @Override
    public List<T> findByIds(Trace t, List<Long> idList) {
        return activity().findByIds(t, idList);
    }

    @Override
    public List<T> findByUuidIds(Trace t, Map<String, Long> uuidIdMap) {
        return activity().findByUuidIds(t, uuidIdMap);
    }

    @Override
    public List<T> find(Trace t, Query query) {
        return activity().find(t, query);
    }

    @Override
    public <X> List<X> query(Trace t, Query query) {
        return activity().query(t, query);
    }

    @Override
    public Page<T> findPage(Trace t, Page<T> page) {
        return activity().findPage(t, page);
    }

    @Override
    public Page<T> findPage4Select(Trace t, Page<T> page) {
        return activity().findPage4Select(t, page);
    }

    @Override
    public <X> Page<X> queryPage(Trace t, Page<X> page) {
        return activity().queryPage(t, page);
    }

}
