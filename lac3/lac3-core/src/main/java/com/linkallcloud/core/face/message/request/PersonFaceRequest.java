package com.linkallcloud.core.face.message.request;

public class PersonFaceRequest extends IdFaceRequest {
    private static final long serialVersionUID = -1800311774979351126L;

    private String operatorType;// 操作者的类型
    private String operatorName;// 操作者的名称
    private String operatorLoginName;// 操作者的登录名

    private String orgId;// 操作者所属的组织ID
    private String orgType;// 操作者所属的组织类型

    public PersonFaceRequest() {
        super();
    }

    public PersonFaceRequest(String id, String uuid, String sessionId, String versn) {
        super(id, uuid, sessionId, versn);
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorLoginName() {
        return operatorLoginName;
    }

    public void setOperatorLoginName(String operatorLoginName) {
        this.operatorLoginName = operatorLoginName;
    }

}
