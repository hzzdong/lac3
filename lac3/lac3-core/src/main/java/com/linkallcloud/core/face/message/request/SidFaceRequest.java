package com.linkallcloud.core.face.message.request;

import java.util.Map;

public class SidFaceRequest extends LoginFaceRequest {
	private static final long serialVersionUID = -3354020104327633698L;

	private Map<String, Long> uuidIds;
	private String type;

	public SidFaceRequest() {
		super();
	}

	public SidFaceRequest(String userType, String userName, Long userId, Long companyId, String companyName) {
		super(userType, userName, userId, companyId, companyName);
	}

	public SidFaceRequest(String userType, String userName, Long userId) {
		super(userType, userName, userId);
	}

	public SidFaceRequest(String token, String versn) {
		super(token, versn);
	}

	public SidFaceRequest(String token) {
		super(token);
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
