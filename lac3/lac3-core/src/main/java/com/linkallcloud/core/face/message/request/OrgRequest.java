package com.linkallcloud.core.face.message.request;

public class OrgRequest extends LoginFaceRequest {
	private static final long serialVersionUID = 5050031561037793604L;

	private Long orgId;
	private String orgUuid;
	private String orgType;// Company:公司，Department:部门

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgUuid() {
		return orgUuid;
	}

	public void setOrgUuid(String orgUuid) {
		this.orgUuid = orgUuid;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

}
