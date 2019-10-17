package com.linkallcloud.core.query;

import java.io.Serializable;

public interface Expression extends Serializable {

	/**
	 * 输出sql的Where对象:sql and args
	 * 
	 * @param where
	 * @param jdbcMappingField
	 */
	void toWhere(QueryHelper where, boolean jdbcMappingField);

	/**
	 * 输出hql的Where对象:sql and args
	 * 
	 * @param where
	 */
	void toWhere4Hql(QueryHelper where);

	/**
	 * 用于生成自动查询
	 * 
	 * 输出hql的Where对象:where sql , args and joins.
	 * 
	 * @param where
	 */
	void toSmartHqlQuery(QueryHelper where);
	
	//String toString();

}
