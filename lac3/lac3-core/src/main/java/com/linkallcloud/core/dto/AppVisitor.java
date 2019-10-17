package com.linkallcloud.core.dto;

public class AppVisitor extends Shadow {
	private static final long serialVersionUID = 8979911305912905771L;

	private String companyId;
	private String companyName;

	private String orgId;// 操作者所属的组织ID
	private String orgName;
	private String orgType;// 操作者所属的组织类型

	private String id;// 操作者的id
	private String uuid;// 操作者的uid,uuid或者其它业务需要的标识
	private String type;// 操作者的类型
	private boolean admin;// 是否管理员
	private String name;// 操作者的名称
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

	private String areaId; // 所在区域
	private String areaName;// 所在区域
	private int areaLevel; // 所在区域level

	private String appId;// 当前登录的app
	private String appUuid;
	private String appName;

	public AppVisitor() {
		super();
	}

	public AppVisitor(String operatorId, String operatorName, String operatorLoginName) {
		super();
		this.id = operatorId;
		this.name = operatorName;
		this.loginName = operatorLoginName;
	}

	public AppVisitor(String operatorId, String operatorName, String operatorLoginName, String ip) {
		this(operatorId, operatorName, operatorLoginName);
		this.ip = ip;
	}

	public AppVisitor(String operatorId, String operatorName, String operatorLoginName, String operatorType,
			String ip) {
		this(operatorId, operatorName, operatorLoginName, ip);
		this.type = operatorType;
	}

	public AppVisitor(String companyId, String companyName, String orgId, String orgName, String orgType,
			String operatorId, String operatorName, String operatorLoginName, String operatorType) {
		this(operatorId, operatorName, operatorLoginName, operatorType, null);
		this.companyId = companyId;
		this.companyName = companyName;
		this.orgId = orgId;
		this.orgName = orgName;
		this.orgType = orgType;
	}

	public AppVisitor(String companyId, String companyName, String orgId, String orgName, String orgType,
			String operatorId, String operatorName, String operatorLoginName, String operatorType, String ip,
			Integer plat) {
		this(operatorId, operatorName, operatorLoginName, operatorType, ip);
		this.companyId = companyId;
		this.companyName = companyName;
		this.orgId = orgId;
		this.orgName = orgName;
		this.orgType = orgType;
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

	public void setAppInfo(String appId, String appUuid, String appName) {
		this.appId = appId;
		this.appUuid = appUuid;
		this.appName = appName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(int areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppUuid() {
		return appUuid;
	}

	public void setAppUuid(String appUuid) {
		this.appUuid = appUuid;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public boolean isAdmin() {
		return admin || this.getLoginName().equals("superadmin");
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
