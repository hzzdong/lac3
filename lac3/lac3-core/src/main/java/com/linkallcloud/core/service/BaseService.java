package com.linkallcloud.core.service;

import java.io.Serializable;
import java.util.Map;

import com.linkallcloud.core.busilog.annotation.ServLog;
import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseService<PK extends Serializable, T extends Domain<PK>, M extends IDao<PK, T>>
        extends BusiLogBaseService<PK, T, M> {

    public BaseService() {
        super();
    }

    // @ServLog(db = true, desc = "新增 [(${domainShowName})]([(${entity.name})]),
    // TID:[(${t.tid})]")
    @Transactional(readOnly = false)
    @Override
    @ServLog(db = true)
    public PK insert(Trace t, T entity) {
        return super.insert(t, entity);
    }

    // @ServLog(db = true, desc = "修改
    // [(${domainShowName})]([(${entity.name})],[(${entity.id})]), TID:[(${t.tid})]")
    @Transactional(readOnly = false)
    @Override
    @ServLog(db = true)
    public boolean update(Trace t, T entity) {
        return super.update(t, entity);
    }

    @Transactional(readOnly = false)
    @Override
    @ServLog(db = true, desc = "修改 [(${domainShowName})]([(${uuidIds})])的状态为([(${status})]), TID:[(${t.tid})]")
    public boolean updates(Trace t, int status, Map<String, PK> uuidIds) {
        return super.updates(t, status, uuidIds);
    }

    @Transactional(readOnly = false)
    @Override
    @ServLog(db = true, desc = "修改 [(${domainShowName})]([(${id})])的状态为([(${status})]), TID:[(${t.tid})]")
    public boolean updateStatus(Trace t, int status, PK id, String uuid) {
        return super.updateStatus(t, status, id, uuid);
    }

    @Transactional(readOnly = false)
    @Override
    @ServLog(db = true, desc = "删除 [(${domainShowName})]([(${id})]), TID:[(${t.tid})]")
    public boolean delete(Trace t, PK id, String uuid) {
        return super.delete(t, id, uuid);
    }

    @Transactional(readOnly = false)
    @Override
    @ServLog(db = true, desc = "删除 [(${domainShowName})]([(${uuidIds})]), TID:[(${t.tid})]")
    public boolean deletes(Trace t, Map<String, PK> uuidIds) {
        return super.deletes(t, uuidIds);
    }

}
