package com.linkallcloud.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.pagination.Page;

public abstract class BusiLogBaseService<PK extends Serializable, T extends Domain<PK>, M extends IDao<PK, T>>
        implements IService<PK, T> {

    protected Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BusiLogBaseService() {
        super();
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[1]);
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

    public abstract M dao();

    protected void beanValidator(T entity) {
        // TODO
    }

    protected void before(Trace t, boolean isNew, T entity) {
        if (Strings.isBlank(entity.getUuid())) {
            entity.setUuid(entity.generateUuid());
        }
        if (isNew) {
            entity.setCreateTime(new Date());
        } else {
            entity.setUpdateTime(new Date());
        }
    }

    protected void after(Trace t, boolean isNew, T entity) {
        //
    }

    /**
     * @param t
     * @param isNew
     * @param entity
     * @param field
     * @param filedValue
     * @return
     * @
     */
    protected boolean exist(Trace t, boolean isNew, T entity, String field, Object filedValue) {
        if (Strings.isBlank(field) || entity == null || filedValue == null) {
            return false;
        }

        Query query = new Query();
        query.addRule(new Equal(field, filedValue));
        // query.put(field, filedValue);
        // query.put(field + "_exact_match", true);// 精确匹配标志，在sql中处理

        List<T> dbEntities = this.find(t, query);

        if (entity.getId() == null) {// 新增
            if (dbEntities != null && dbEntities.size() > 0) {
                return true;
            }
        } else {// 修改
            if (dbEntities != null && dbEntities.size() > 1) {
                return true;
            } else if (dbEntities != null && dbEntities.size() == 1) {
                if (!dbEntities.get(0).getId().equals(entity.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    @Override
    public PK insert(Trace t, T entity) {
        if (entity == null) {
            log.error("T entity 不能为null。");
            throw new RuntimeException("T entity 不能为null。");
        }
        beanValidator(entity);

        // if(entity.getId().getClass().getSimpleName().equals("String")) {
        // String idValue = (String) entity.getId();
        // if (Strings.isBlank(idValue)) {
        // entity.setId((PK)entity.generateUuid());
        // }
        // }

        before(t, true, entity);

        int rows = dao().insert(t, entity);

        after(t, true, entity);

        if (rows > 0) {
            log.debug("insert 成功，tid：" + t.getTid() + ", id:" + entity.getId());
        } else {
            log.error("insert 失败，tid：" + t.getTid() + ", id:" + entity.getId());
        }
        return entity.getId();
    }

    @Transactional
    @Override
    public boolean update(Trace t, T entity) {
        beanValidator(entity);
        before(t, false, entity);
        entity.setUpdateTime(new Date());
        int rows = dao().update(t, entity);
        after(t, false, entity);
        if (rows > 0) {
            log.debug("update 成功，tid：" + t.getTid() + ", id:" + entity.getId());
        } else {
            log.error("update 失败，tid：" + t.getTid() + ", id:" + entity.getId());
        }
        return retBool(rows);
    }

    @Transactional
    @Override
    public boolean updates(Trace t, int status, Map<String, PK> uuidIds) {
        List<T> checkedEntities = findByUuidIds(t, uuidIds);
        if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
            List<PK> ids = Domains.parseIds(uuidIds);
            int rows = dao().updates(t, status, ids);
            if (rows > 0) {
                log.debug("updates 成功，tid：" + t.getTid() + ", ids:" + JSON.toJSONString(ids));
            } else {
                log.error("updates 失败，tid：" + t.getTid() + ", ids:" + JSON.toJSONString(ids));
            }
            return retBool(rows);
        }
        log.error("updates 失败，tid：" + t.getTid());
        return false;
    }

    @Transactional
    @Override
    public boolean updateStatus(Trace t, int status, PK id, String uuid) {
        int rows = dao().updateStatus(t, status, id, uuid);
        if (rows > 0) {
            log.debug("updateStatus 成功，tid：" + t.getTid() + ", id:" + id);
        } else {
            log.error("updateStatus 失败，tid：" + t.getTid() + ", id:" + id);
        }
        return retBool(rows);
    }

    @Transactional
    @Override
    public boolean delete(Trace t, PK id, String uuid) {
        int rows = dao().delete(t, id, uuid);
        if (rows > 0) {
            log.debug("delete 成功，tid：" + t.getTid() + ", id:" + id);
        } else {
            log.error("delete 失败，tid：" + t.getTid() + ", id:" + id);
        }
        return retBool(rows);
    }

    @Transactional
    @Override
    public boolean deletes(Trace t, Map<String, PK> uuidIds) {
        List<T> checkedEntities = findByUuidIds(t, uuidIds);
        if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
            List<PK> ids = Domains.parseIds(uuidIds);
            int rows = dao().deletes(t, ids);
            if (rows > 0) {
                log.debug("deletes 成功，tid：" + t.getTid() + ", ids:" + JSON.toJSONString(ids));
            } else {
                log.error("deletes 失败，tid：" + t.getTid() + ", ids:" + JSON.toJSONString(ids));
            }
            return retBool(rows);
        }
        log.error("deletes 失败，tid：" + t.getTid());
        return false;
    }

    @Override
    public T fetchById(Trace t, PK id) {
        return dao().fetchById(t, id);
    }

    @Override
    public T fetchByIdUuid(Trace t, PK id, String uuid) {
        return dao().fetchByIdUuid(t, id, uuid);
    }

    @Override
    public List<T> findByIds(Trace t, List<PK> idList) {
        return dao().findByIds(t, idList);
    }

    @Override
    public List<T> findByUuidIds(Trace t, Map<String, PK> uuidIdMap) {
        List<PK> ids = Domains.parseIds(uuidIdMap);
        if (ids != null && ids.size() > 0) {
            List<T> entities = findByIds(t, ids);
            if (entities != null && !entities.isEmpty()) {
                List<T> results = new ArrayList<T>();
                for (T entity : entities) {
                    if (entity.getUuid() != null && entity.getId().equals(uuidIdMap.get(entity.getUuid()))) {
                        results.add(entity);
                    }
                }
                return results;
            }
        }
        return null;
    }

    @Override
    public List<T> find(Trace t, Query query) {
        return dao().find(t, query);
    }

    @Override
    public Page<PK, T> findPage(Trace t, Page<PK, T> page) {
        page.checkPageParameters();

        try {
            PageHelper.startPage(page.getPageNum(), page.getLength());
            List<T> list = dao().findPage(t, page);
            if (list instanceof com.github.pagehelper.Page) {
                page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
                page.checkPageParameters();
                page.setRecordsFiltered(page.getRecordsTotal());
                page.addDataAll(list);
            }
            return page;
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public Page<PK, T> findPage4Select(Trace t, Page<PK, T> page) {
        page.checkPageParameters();
        try {
            PageHelper.startPage(page.getPageNum(), page.getLength());
            List<T> list = dao().findPage4Select(t, page);
            if (list instanceof com.github.pagehelper.Page) {
                page.setRecordsTotal(((com.github.pagehelper.Page<T>) list).getTotal());
                page.checkPageParameters();
                page.setRecordsFiltered(page.getRecordsTotal());
                page.addDataAll(list);
            }
            return page;
        } finally {
            PageHelper.clearPage();
        }
    }

    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(int result) {
        return result >= 1;
    }

}
