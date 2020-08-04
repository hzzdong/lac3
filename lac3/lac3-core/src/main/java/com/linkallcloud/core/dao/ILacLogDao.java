package com.linkallcloud.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;

public interface ILacLogDao<T extends BusiLog> {
	
	/**
     * 插入一条记录
     *
     * @param t      trace，含业务流水ID
     * @param entity 实体对象
     * @return boolean
     */
    int insert(@Param("t") Trace t, @Param("entity") T entity);
    
    /**
     * 根据 id, uuid 删除
     *
     * @param t    trace，含业务流水ID
     * @param id   主键ID
     * @param uuid uuid
     * @return boolean
     */
    int delete(@Param("t") Trace t, @Param("id") Long id, @Param("uuid") String uuid);
    
    /**
     * 根据 ID 查询
     *
     * @param t  trace，含业务流水ID
     * @param id 主键ID
     * @return T
     */
    T fetchById(@Param("t") Trace t, @Param("id") Long id);
    
    /**
     * 根据查询条件，查找实体列表
     *
     * @param t     trace，含业务流水ID
     * @param query 查询/排序条件
     * @return Entities list
     */
    List<T> find(@Param("t") Trace t, @Param("query") Query query);
    
    /**
     * 分页查询
     *
     * @param t trace，含业务流水ID
     * @return page
     */
    List<T> findPage(@Param("t") Trace t, @Param("page") Page<T> page);

}
