package com.linkallcloud.core.face.message.request;

public class TreeFaceRequest extends LoginFaceRequest {
    private static final long serialVersionUID = 7582160263902693131L;

    private String parentId;

    public TreeFaceRequest() {
        super();
    }

    public TreeFaceRequest(String userType, String loginName, Long userId, Long companyId, String companyName) {
        super(userType, loginName, userId, companyId, companyName);
    }

    public TreeFaceRequest(String userType, String loginName, Long userId) {
        super(userType, loginName, userId);
    }

    public TreeFaceRequest(String token, String versn) {
        super(token, versn);
    }

    public TreeFaceRequest(String token) {
        super(token);
    }

    public TreeFaceRequest(String parentId, String sessionId, String versn) {
        super(sessionId, versn);
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
