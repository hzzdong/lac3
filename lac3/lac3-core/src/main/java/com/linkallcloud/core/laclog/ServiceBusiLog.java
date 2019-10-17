package com.linkallcloud.core.laclog;

import java.io.Serializable;

public class ServiceBusiLog<PK extends Serializable> extends BusiLog<PK> {
	private static final long serialVersionUID = 7855862585657490739L;
	
	private String ip;// 调用方ip

	public ServiceBusiLog() {
		super();
	}

	public ServiceBusiLog(PK id, String uuid) {
		super(id, uuid);
	}

	public ServiceBusiLog(PK id) {
		super(id);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
