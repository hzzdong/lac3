package com.linkallcloud.core.face.message.request;

import java.util.Map;

public class StatussFaceRequest extends LoginFaceRequest {
	private static final long serialVersionUID = -926197193638544055L;

	private int status;
	private Map<String, Long> uuidIds;
	private String type;

	public StatussFaceRequest() {
		super();
	}

	public StatussFaceRequest(String userType, String userName, Long userId, Long companyId, String companyName) {
		super(userType, userName, userId, companyId, companyName);
	}

	public StatussFaceRequest(String userType, String userName, Long userId) {
		super(userType, userName, userId);
	}

	public StatussFaceRequest(String token, String versn) {
		super(token, versn);
	}

	public StatussFaceRequest(String token) {
		super(token);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, Long> getUuidIds() {
		return uuidIds;
	}

	public void setUuidIds(Map<String, Long> uuidIds) {
		this.uuidIds = uuidIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
