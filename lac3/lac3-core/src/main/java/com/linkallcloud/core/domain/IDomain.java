package com.linkallcloud.core.domain;

import java.io.Serializable;
import java.util.Date;

public interface IDomain extends Serializable {

	/**
	 * 状态
	 */
	public static final int STATUS_NORMAL = 0;// 0:正常
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

	int getStatus();
	void setStatus(int status);

	String generateUuid();

}
