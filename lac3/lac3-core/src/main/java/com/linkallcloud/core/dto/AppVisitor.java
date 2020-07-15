package com.linkallcloud.core.dto;

public class AppVisitor extends Shadow {
    private static final long serialVersionUID = 8979911305912905771L;

    private Sid companyId;
    private Sid orgId;// 操作者所属的组织ID

    private Sid id;// 操作者的id
    private String type;// 操作者的类型
    private boolean admin;// 是否管理员
    private String loginName;// 操作者的登录名
    private String phone;

    private String ip;// 操作者的登陆ip

    private Boolean mobile;// 是否移动设备
    private String mobileBrand;// 手机品牌信息
    private String os;// 操作系统
    private String osVersion;// 操作系统版本
    private String browser;// 浏览器
    private String browserVersion;// 浏览器版本
    private String ua;// navigator.userAgent

    private Sid areaId; // 所在区域
    private int areaLevel; // 所在区域level

    private Sid appId;// 当前登录的app

    public AppVisitor() {
        super();
    }

    public AppVisitor(Long operatorId, String operatorName, String operatorLoginName) {
        super();
        this.id = new Sid(operatorId, null, operatorName);
        this.loginName = operatorLoginName;
    }

    public AppVisitor(Long operatorId, String operatorName, String operatorLoginName, String ip) {
        this(operatorId, operatorName, operatorLoginName);
        this.ip = ip;
    }

    public AppVisitor(Long operatorId, String operatorName, String operatorLoginName, String operatorType,
                      String ip) {
        this(operatorId, operatorName, operatorLoginName, ip);
        this.type = operatorType;
    }

    public AppVisitor(Long companyId, String companyUuid, String companyName, Long orgId, String orgUuid, String orgName,
                      Long operatorId, String operatorName, String operatorLoginName, String operatorType, String ip) {
        this(operatorId, operatorName, operatorLoginName, operatorType, ip);
        this.companyId = new Sid(companyId, companyUuid, companyName);
        this.orgId = new Sid(orgId, orgUuid, orgName);
        ;
    }

    public void setClient(Client client) {
        this.ip = client.getIp();
        this.mobile = client.getMobile();
        this.mobileBrand = client.getMobileBrand();
        this.os = client.getOs();
        this.osVersion = client.getOsVersion();
        this.browser = client.getBrowser();
        this.browserVersion = client.getBrowserVersion();
        this.ua = client.getUa();
    }

    public void setAppInfo(Long appId, String appUuid, String appName) {
        this.appId = new Sid(appId, appUuid, appName);
    }

    public Sid getCompanyId() {
        return companyId;
    }

    public Long companyId() {
        return companyId == null ? null : companyId.getId();
    }

    public String companyUuid() {
        return companyId == null ? null : companyId.getUuid();
    }

    public String companyName() {
        return companyId == null ? null : companyId.getName();
    }

    public void setCompanyId(Sid companyId) {
        this.companyId = companyId;
    }

    public Sid getOrgId() {
        return orgId;
    }

    public Long orgId() {
        return orgId == null ? null : orgId.getId();
    }

    public String orgUuid() {
        return orgId == null ? null : orgId.getUuid();
    }

    public String orgName() {
        return orgId == null ? null : orgId.getName();
    }


    public void setOrgId(Sid orgId) {
        this.orgId = orgId;
    }

    public Sid getId() {
        return id;
    }

    public Long id() {
        return id == null ? null : id.getId();
    }

    public String uuid() {
        return id == null ? null : id.getUuid();
    }

    public String name() {
        return id == null ? null : id.getName();
    }

    public void setId(Sid id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public Sid getAreaId() {
        return areaId;
    }

    public Long areaId() {
        return areaId == null ? null : areaId.getId();
    }

    public String areaUuid() {
        return areaId == null ? null : areaId.getUuid();
    }

    public String areaCode() {
        return areaId == null ? null : areaId.getCode();
    }

    public String areaName() {
        return areaId == null ? null : areaId.getName();
    }

    public void setAreaId(Sid areaId) {
        this.areaId = areaId;
    }

    public int getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(int areaLevel) {
        this.areaLevel = areaLevel;
    }

    public Sid getAppId() {
        return appId;
    }

    public Long appId() {
        return appId == null ? null : appId.getId();
    }

    public String appUuid() {
        return appId == null ? null : appId.getUuid();
    }

    public String appCode() {
        return appId == null ? null : appId.getCode();
    }

    public String appName() {
        return appId == null ? null : appId.getName();
    }

    public void setAppId(Sid appId) {
        this.appId = appId;
    }

    public boolean isAdmin() {
        return admin || this.getLoginName().equals("superadmin");
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
