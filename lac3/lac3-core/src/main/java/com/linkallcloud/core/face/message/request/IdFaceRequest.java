package com.linkallcloud.core.face.message.request;

import com.linkallcloud.core.dto.Sid;

public class IdFaceRequest extends LoginFaceRequest {
	private static final long serialVersionUID = 8183192881974198735L;

	private Long id;
	private String uuid;
	private String type;

	public IdFaceRequest() {
		super();
	}

	public IdFaceRequest(String userType, String loginName, Long userId, Long companyId, String companyName) {
		super(userType, loginName, userId, companyId, companyName);
	}

	public IdFaceRequest(String userType, String loginName, Long userId) {
		super(userType, loginName, userId);
	}

	public IdFaceRequest(Long id, String uuid) {
		super();
		this.id = id;
		this.uuid = uuid;
	}

	public IdFaceRequest(Long id, String uuid, String token, String versn) {
		super(token, versn);
		this.id = id;
		this.uuid = uuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Sid sid() {
		return new Sid(this.getId(), this.getUuid(), this.getType(), null);
	}

}
