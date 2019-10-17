package com.linkallcloud.core.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;

public interface IDao<PK extends Serializable, T extends Domain<PK>> {

    /**
     * 插入一条记录
     *
     * @param t
     *            trace，含业务流水ID
     * @param entity
     *            实体对象
     * @return boolean
     */
    int insert(@Param("t") Trace t, @Param("entity") T entity);

    /**
     * 根据 ID 修改
     *
     * @param t
     *            trace，含业务流水ID
     * @param entity
     *            实体对象
     * @return boolean
     */
    int update(@Param("t") Trace t, @Param("entity") T entity);

    /**
     * 更新状态
     * 
     * @param t
     * @param status
     * @param id
     * @param uuid
     * @return
     */
    int updateStatus(@Param("t") Trace t, @Param("status") int status, @Param("id") PK id, @Param("uuid") String uuid);

    /**
     * 批量更新状态
     * 
     * @param t
     *            trace，含业务流水ID
     * @param status
     *            状态
     * @param ids
     * @return
     */
    int updates(@Param("t") Trace t, @Param("status") int status, @Param("ids") List<PK> ids);

    /**
     * 更新code
     * 
     * @param t
     * @param id
     * @param code
     * @return
     */
    int updateCode(@Param("t") Trace t, @Param("id") PK id, @Param("code") String code);

    /**
     * 根据 id, uuid 删除
     *
     * @param t
     *            trace，含业务流水ID
     * @param id
     *            主键ID
     * @param uuid
     *            uuid
     * @return boolean
     */
    int delete(@Param("t") Trace t, @Param("id") PK id, @Param("uuid") String uuid);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param t
     *            trace，含业务流水ID
     * @param ids
     * @return boolean
     */
    int deletes(@Param("t") Trace t, @Param("ids") List<PK> ids);

    /**
     * 根据 ID 查询
     *
     * @param t
     *            trace，含业务流水ID
     * @param id
     *            主键ID
     * @return T
     */
    T fetchById(@Param("t") Trace t, @Param("id") PK id);

    /**
     * 根据 id,UUID 查询
     *
     * @param t
     *            trace，含业务流水ID
     * @param id
     *            主键ID
     * @param uuid
     *            uuid
     * @return T
     */
    T fetchByIdUuid(@Param("t") Trace t, @Param("id") PK id, @Param("uuid") String uuid);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param t
     *            trace，含业务流水ID
     * @param idList
     *            主键ID列表
     * @return List<T>
     */
    List<T> findByIds(@Param("t") Trace t, @Param("ids") List<PK> ids);

    /**
     * 根据查询条件，查找实体列表
     * 
     * @param t
     *            trace，含业务流水ID
     * @param cnds
     *            查询条件
     * @param orderby
     *            排序条件
     * @return Entities list
     * 
     */
    List<T> find(@Param("t") Trace t, @Param("query") Query query);

    /**
     * 分页查询
     * 
     * @param t
     *            trace，含业务流水ID
     * @param cnds
     * @return page
     */
    List<T> findPage(@Param("t") Trace t, @Param("page") Page<PK, T> page);

    /**
     * 分页查询
     * 
     * @param t
     *            trace，含业务流水ID
     * @param cnds
     * @return page
     */
    List<T> findPage4Select(@Param("t") Trace t, @Param("page") Page<PK, T> page);

}
