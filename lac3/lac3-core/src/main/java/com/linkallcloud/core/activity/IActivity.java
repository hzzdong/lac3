package com.linkallcloud.core.activity;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * 服务接口（仅暴露给内部使用）
 *
 * @param <T>
 */
public interface IActivity<T> {

    Class<T> getDomainClass();

    /**
     * entity是否已经存在
     *
     * @param t
     * @param entity
     * @param field
     * @param filedValue
     * @return
     */
    boolean exist(Trace t, T entity, String field, Object filedValue);

    /**
     * 插入一条记录
     *
     * @param t      trace，含业务流水ID
     * @param entity 实体对象
     * @return boolean
     */
    Long insert(Trace t, T entity);

    /**
     * 根据 ID 修改
     *
     * @param t      trace，含业务流水ID
     * @param entity 实体对象
     * @return boolean
     */
    boolean update(Trace t, T entity);

    /**
     * 批量更新状态
     *
     * @param t       trace，含业务流水ID
     * @param status  状态
     * @param uuidIds map(uuid,id)
     * @return
     */
    boolean updates(Trace t, int status, Map<String, Long> uuidIds);

    /**
     * 更新状态
     *
     * @param t      trace，含业务流水ID
     * @param status
     * @param id
     * @param uuid
     * @return
     */
    boolean updateStatus(Trace t, int status, Long id, String uuid);

    /**
     * 根据 id, uuid 删除
     *
     * @param t    trace，含业务流水ID
     * @param id   主键ID
     * @param uuid uuid
     * @return boolean
     */
    boolean delete(Trace t, Long id, String uuid);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param t         trace，含业务流水ID
     * @param uuidIdMap map(uuid,id)
     * @return boolean
     */
    boolean deletes(Trace t, Map<String, Long> uuidIdMap);

    /**
     * 根据 ID 查询
     *
     * @param t  trace，含业务流水ID
     * @param id 主键ID
     * @return T
     */
    T fetchById(Trace t, Long id);

    /**
     * 根据 id,UUID 查询
     *
     * @param t    trace，含业务流水ID
     * @param id   主键ID
     * @param uuid uuid
     * @return T
     */
    T fetchByIdUuid(Trace t, Long id, String uuid);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param t      trace，含业务流水ID
     * @param idList 主键ID列表
     * @return List<T>
     */
    List<T> findByIds(Trace t, List<Long> idList);

    /**
     * 查询（根据UUID 批量查询）
     *
     * @param t         trace，含业务流水ID
     * @param uuidIdMap map(uuid,id)
     * @return List<T>
     */
    List<T> findByUuidIds(Trace t, Map<String, Long> uuidIdMap);

    /**
     * 根据查询条件，查找实体列表
     *
     * @param t     trace，含业务流水ID
     * @param query 查询条件
     * @return Entities list
     */
    List<T> find(Trace t, Query query);

    /**
     * 根据查询条件，查找实体列表
     *
     * @param t     trace，含业务流水ID
     * @param query 查询/排序条件
     * @return Entities list
     */
    <X> List<X> query(Trace t, Query query);

    /**
     * 分页查询
     *
     * @param t trace，含业务流水ID
     * @return page
     */
    Page<T> findPage(Trace t, Page<T> page);

    /**
     * 分页查询
     *
     * @param t trace，含业务流水ID
     * @return page
     */
    Page<T> findPage4Select(Trace t, Page<T> page);

    /**
     * 分页查询
     *
     * @param t trace，含业务流水ID
     * @return page
     */
    <X> Page<X> queryPage(Trace t, Page<X> page);
}
