package com.linkallcloud.core.activity;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BaseActivity<T extends Domain, D extends IDao<T>> implements IActivity<T> {

    protected Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BaseActivity() {
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

    public abstract D dao();

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
     * @param entity
     * @param field
     * @param filedValue
     * @return
     * @
     */
    public boolean exist(Trace t, T entity, String field, Object filedValue) {
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

    @Override
    public Long insert(Trace t, T entity) {
        if (entity == null) {
            log.error("T entity 不能为null。");
            throw new BizException(Exceptions.CODE_ERROR_ADD, "T entity 不能为null。");
        }

        beanValidator(entity);
        before(t, true, entity);
        int rows = dao().insert(t, entity);
        after(t, true, entity);

        if (rows > 0) {
            log.debugf("%s, insert 成功，tid：%s, id:%s", entity.getClass().getName(), t.getTid(), entity.getId());
        } else {
            log.debugf("%s, insert 失败，tid：%s, id:%s", entity.getClass().getName(), t.getTid(), entity.getId());
        }
        return entity.getId();
    }

    @Override
    public boolean update(Trace t, T entity) {
        beanValidator(entity);
        before(t, false, entity);
        entity.setUpdateTime(new Date());
        int rows = dao().update(t, entity);
        after(t, false, entity);
        if (rows > 0) {
            log.debugf("%s, update 成功，tid：%s, id:%s", entity.getClass().getName(), t.getTid(), entity.getId());
        } else {
            log.debugf("%s, update 失败，tid：%s, id:%s", entity.getClass().getName(), t.getTid(), entity.getId());
        }
        return retBool(rows);
    }

    @Override
    public boolean updates(Trace t, int status, Map<String, Long> uuidIds) {
        List<T> checkedEntities = findByUuidIds(t, uuidIds);
        if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
            List<Long> ids = Domains.parseIds(uuidIds);
            int rows = dao().updates(t, status, ids);
            if (rows > 0) {
                log.debugf("%s, update 成功，tid：%s, ids:%s", getDomainClass().getName(), t.getTid(), JSON.toJSONString(ids));
            } else {
                log.debugf("%s, update 失败，tid：%s, ids:%s", getDomainClass().getName(), t.getTid(), JSON.toJSONString(ids));
            }
            return retBool(rows);
        }
        log.error("updates 失败，tid：" + t.getTid());
        return false;
    }

    @Override
    public boolean updateStatus(Trace t, int status, Long id, String uuid) {
        int rows = dao().updateStatus(t, status, id, uuid);
        if (rows > 0) {
            log.debugf("%s, updateStatus 成功，tid：%s, id:%s", getDomainClass().getName(), t.getTid(), id);
        } else {
            log.debugf("%s, updateStatus 失败，tid：%s, id:%s", getDomainClass().getName(), t.getTid(), id);
        }
        return retBool(rows);
    }

    @Override
    public boolean delete(Trace t, Long id, String uuid) {
        int rows = dao().delete(t, id, uuid);
        if (rows > 0) {
            log.debugf("%s, delete 成功，tid：%s, id:%s", getDomainClass().getName(), t.getTid(), id);
        } else {
            log.debugf("%s, delete 失败，tid：%s, id:%s", getDomainClass().getName(), t.getTid(), id);
        }
        return retBool(rows);
    }

    @Override
    public boolean deletes(Trace t, Map<String, Long> uuidIds) {
        List<T> checkedEntities = findByUuidIds(t, uuidIds);
        if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == uuidIds.size()) {
            List<Long> ids = Domains.parseIds(uuidIds);
            int rows = dao().deletes(t, ids);
            if (rows > 0) {
                log.debugf("%s, deletes 成功，tid：%s, ids:%s", getDomainClass().getName(), t.getTid(), JSON.toJSONString(ids));
            } else {
                log.debugf("%s, deletes 失败，tid：%s, ids:%s", getDomainClass().getName(), t.getTid(), JSON.toJSONString(ids));
            }
            return retBool(rows);
        }
        log.error("deletes 失败，tid：" + t.getTid());
        return false;
    }

    @Override
    public T fetchById(Trace t, Long id) {
        return dao().fetchById(t, id);
    }

    @Override
    public T fetchByIdUuid(Trace t, Long id, String uuid) {
        return dao().fetchByIdUuid(t, id, uuid);
    }

    @Override
    public List<T> findByIds(Trace t, List<Long> idList) {
        return dao().findByIds(t, idList);
    }

    @Override
    public List<T> findByUuidIds(Trace t, Map<String, Long> uuidIdMap) {
        List<Long> ids = Domains.parseIds(uuidIdMap);
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
    public <X> List<X> query(Trace t, Query query) {
        return dao().query(t, query);
    }

    @Override
    public Page<T> findPage(Trace t, Page<T> page) {
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
    public Page<T> findPage4Select(Trace t, Page<T> page) {
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

    @Override
    public <X> Page<X> queryPage(Trace t, Page<X> page) {
        page.checkPageParameters();
        try {
            PageHelper.startPage(page.getPageNum(), page.getLength());
            List<X> list = dao().queryPage(t, page);
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
