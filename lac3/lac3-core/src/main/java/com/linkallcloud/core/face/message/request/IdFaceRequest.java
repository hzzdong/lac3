package com.linkallcloud.core.face.message.request;

public class IdFaceRequest extends LoginFaceRequest {
    private static final long serialVersionUID = 8183192881974198735L;

    private Long id;
    private String uuid;

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

}
