package com.linkallcloud.core.face.message.request;

public class LoginFaceRequest extends FaceRequest {
    private static final long serialVersionUID = -1980382022809242376L;

    private Long userId;
    private String userType;
    private String userName;

    private Long companyId;
    private String companyName;

    public LoginFaceRequest() {
        super();
    }

    public LoginFaceRequest(String userType, String userName, Long userId) {
        super();
        this.userType = userType;
        this.userName = userName;
        this.userId = userId;
    }

    public LoginFaceRequest(String userType, String userName, Long userId, Long companyId, String companyName) {
        super();
        this.userType = userType;
        this.userName = userName;
        this.userId = userId;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public LoginFaceRequest(String token, String versn) {
        super(token, versn);
    }

    public LoginFaceRequest(String token) {
        super(token);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
