package com.linkallcloud.core.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.pagination.Page;

/**
 * 服务接口（仅暴露给内部使用）
 *
 * @param <PK>
 * @param <T>
 */
public interface IManager<PK extends Serializable, T extends Domain<PK>> {

    /**
     * 插入一条记录
     *
     * @param t      trace，含业务流水ID
     * @param entity 实体对象
     * @return boolean
     */
    PK insert(Trace t, T entity);

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
    boolean updates(Trace t, int status, Map<String, PK> uuidIds);

    /**
     * 更新状态
     *
     * @param t      trace，含业务流水ID
     * @param status
     * @param id
     * @param uuid
     * @return
     */
    boolean updateStatus(Trace t, int status, PK id, String uuid);

    /**
     * 根据 id, uuid 删除
     *
     * @param t    trace，含业务流水ID
     * @param id   主键ID
     * @param uuid uuid
     * @return boolean
     */
    boolean delete(Trace t, PK id, String uuid);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param t         trace，含业务流水ID
     * @param uuidIdMap map(uuid,id)
     * @return boolean
     */
    boolean deletes(Trace t, Map<String, PK> uuidIdMap);

    /**
     * 根据 ID 查询
     *
     * @param t  trace，含业务流水ID
     * @param id 主键ID
     * @return T
     */
    T fetchById(Trace t, PK id);

    /**
     * 根据 id,UUID 查询
     *
     * @param t    trace，含业务流水ID
     * @param id   主键ID
     * @param uuid uuid
     * @return T
     */
    T fetchByIdUuid(Trace t, PK id, String uuid);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param t      trace，含业务流水ID
     * @param idList 主键ID列表
     * @return List<T>
     */
    List<T> findByIds(Trace t, List<PK> idList);

    /**
     * 查询（根据UUID 批量查询）
     *
     * @param t         trace，含业务流水ID
     * @param uuidIdMap map(uuid,id)
     * @return List<T>
     */
    List<T> findByUuidIds(Trace t, Map<String, PK> uuidIdMap);

    /**
     * 根据查询条件，查找实体列表
     *
     * @param t       trace，含业务流水ID
     * @param query   查询条件
     * @return Entities list
     */
    List<T> find(Trace t, Query query);

    /**
     * 分页查询
     *
     * @param t     trace，含业务流水ID
     * @return page
     */
    Page<PK, T> findPage(Trace t, Page<PK, T> page);

    /**
     * 分页查询
     *
     * @param t     trace，含业务流水ID
     * @return page
     */
    Page<PK, T> findPage4Select(Trace t, Page<PK, T> page);

}
