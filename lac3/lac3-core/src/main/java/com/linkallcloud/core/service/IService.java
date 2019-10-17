package com.linkallcloud.core.service;

import java.io.Serializable;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.manager.IManager;

/**
 * service接口，在本层支持管理数据库事务
 * 
 * @param <PK>
 * @param <T>
 */
public interface IService<PK extends Serializable, T extends Domain<PK>> extends IManager<PK, T> {

	Class<T> getDomainClass();

}
