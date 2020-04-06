package com.linkallcloud.core.face.message.request;

public class StatusFaceRequest extends IdFaceRequest {
    private static final long serialVersionUID = -8823388221411653913L;

    private int status;

    public StatusFaceRequest() {
        super();
    }

    public StatusFaceRequest(String userType, String loginName, Long userId, Long companyId, String companyName, int status) {
        super(userType, loginName, userId, companyId, companyName);
        this.status = status;
    }

    public StatusFaceRequest(String userType, String loginName, Long userId, int status) {
        super(userType, loginName, userId);
        this.status = status;
    }

    public StatusFaceRequest(String id, String uuid, int status) {
        super(id, uuid);
        this.status = status;
    }

    public StatusFaceRequest(String id, String uuid, String token, String versn, int status) {
        super(id, uuid, token, versn);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
