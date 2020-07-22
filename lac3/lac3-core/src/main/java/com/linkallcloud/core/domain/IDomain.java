package com.linkallcloud.core.domain;

import java.io.Serializable;
import java.util.Date;

public interface IDomain extends Serializable {

	/**
	 * 状态
	 */
	public static final int STATUS_NORMAL = 0;// 0:正常
	public static final int STATUS_LOCKED = 1;// 1:锁定/禁用
	public static final int STATUS_QUOTE = 7;// 引用/兼职
	public static final int STATUS_CANCEL = 8;// 注销/离职
	public static final int STATUS_DELETE = 9;// 删除

	Long getId();
	void setId(Long id);

	String getUuid();
	void setUuid(String uuid);

	Date getCreateTime();
	void setCreateTime(Date createTime);

	// PK getCreateUserId();
	// void setCreateUserId(PK createUserId);

	Date getUpdateTime();
	void setUpdateTime(Date updateTime);

	// PK getUpdateUserId();
	// void setUpdateUserId(PK updateUserId);

	Integer getStatus();
	void setStatus(Integer status);
	boolean isValid();

	String generateUuid();

}
