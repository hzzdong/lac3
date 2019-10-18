package com.linkallcloud.core.service;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.domain.Domain;

/**
 * service接口，在本层支持管理数据库事务
 *
 * @param <T>
 */
public interface IService<T extends Domain> extends IActivity<T> {

}
