package com.linkallcloud.core.laclog;

import com.linkallcloud.core.enums.LoginMode;
import com.linkallcloud.core.www.ISessionUser;

public class WebBusiLog extends BusiLog {
	private static final long serialVersionUID = -1697277385596623092L;

	/**
	 * 操作者信息
	 ***********************/
	private Long orgId;// 操作者所属的组织ID
	private String orgType;// 操作者所属的组织类型
	private Long operatorId;// 操作者的id
	private String operatorAccount;// 操作者的登录名

	/**
	 * 操作终端信息
	 ***********************/
	private String ip;// 操作者的登陆ip
	private String url;// url
	private Boolean mobile;// 是否移动设备
	private String mobileBrand;// 手机品牌信息
	private String os;// 操作系统
	private String osVersion;// 操作系统版本
	private String browser;// 浏览器
	private String browserVersion;// 浏览器版本
	private String ua;// navigator.userAgent

	public WebBusiLog() {
		super();
	}

	public WebBusiLog(Long id, String uuid) {
		super(id, uuid);
	}

	public WebBusiLog(Long id) {
		super(id);
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

//    public void setOperator(AppVisitor av) {
//        if (av != null) {
//            this.operatorId = av.id();
//            this.operatorAccount = av.getLoginName();
//            this.orgId = av.companyId();//.orgId();
//            this.orgType = av.getType();
//        }
//    }

	public void setOperator(ISessionUser su) {
		if (su != null) {
			this.operatorId = su.getSid().getId();
			if (LoginMode.Proxy.getCode().equals(su.getLoginMode()) && su.getSrcUser() != null) {
				this.operatorAccount = su.getSrcUser().getLoginName() + " 代 " + su.getLoginName();
			} else {
				this.operatorAccount = su.getLoginName();
			}
			this.orgId = su.getCompany().getId();// .orgId();
			this.orgType = su.getUserType();
		}
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorAccount() {
		return operatorAccount;
	}

	public void setOperatorAccount(String operatorAccount) {
		this.operatorAccount = operatorAccount;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

}
