package com.linkallcloud.core.manager;

import com.linkallcloud.core.activity.IActivity;
import com.linkallcloud.core.domain.Domain;

/**
 * 服务接口（仅暴露给内部使用）
 *
 * @param <T>
 */
public interface IManager<T extends Domain> extends IActivity<T> {

}
