package com.linkallcloud.core.activity;

import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;

public interface ILacLogActivity<T extends BusiLog> {

	/**
	 * 插入一条记录
	 *
	 * @param t      trace，含业务流水ID
	 * @param entity 实体对象
	 * @return boolean
	 */
	Long insert(Trace t, T entity);

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
	 * 根据 ID 查询
	 *
	 * @param t  trace，含业务流水ID
	 * @param id 主键ID
	 * @return T
	 */
	T fetchById(Trace t, Long id);

	/**
	 * 根据查询条件，查找实体列表
	 *
	 * @param t     trace，含业务流水ID
	 * @param query 查询条件
	 * @return Entities list
	 */
	List<T> find(Trace t, Query query);

	/**
	 * 分页查询
	 *
	 * @param t trace，含业务流水ID
	 * @return page
	 */
	Page<T> findPage(Trace t, Page<T> page);

}
