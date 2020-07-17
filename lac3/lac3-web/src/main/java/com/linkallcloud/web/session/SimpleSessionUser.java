package com.linkallcloud.web.session;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.enums.LoginMode;
import com.linkallcloud.core.www.ISimpleSessionUser;

public class SimpleSessionUser implements ISimpleSessionUser {
	private static final long serialVersionUID = 2593285685969044880L;

	private Sid sid;
	private String loginName;
	private boolean admin;// 是否管理员
	private String userType;// 用户类型

	private Sid company;// 当前登录的company
	private Sid org;// 当前登录的org
	private Sid app;// 当前登录的app

	private Integer loginMode;// 登录模式，normal:普通模式,proxy:代维模式
	private ISimpleSessionUser srcUser;

	public SimpleSessionUser() {
		super();
	}

	public SimpleSessionUser(Long id, String uuid, String loginName, String name, String userType) {
		this.sid = new Sid(id, uuid, loginName, name);
		this.loginName = loginName;
		this.userType = userType;
	}

	public SimpleSessionUser(Long id, String uuid, String loginName, String name, String userType, Long companyId,
			String companyUuid, String companyCode, String companyName, Long orgId, String orgUuid, String orgCode,
			String orgName) {
		this(id, uuid, loginName, name, userType);
		this.setCompanyInfo(companyId, companyUuid, companyCode, companyName);
		this.setOrgInfo(orgId, orgUuid, orgCode, orgName);
	}

	public void setCompanyInfo(Long id, String uuid, String code, String name) {
		this.company = new Sid(id, uuid, code, name);
	}

	public void setOrgInfo(Long id, String uuid, String code, String name) {
		this.org = new Sid(id, uuid, code, name);
	}

	public void setAppInfo(Long appId, String appUuid, String appCode, String appName) {
		this.app = new Sid(appId, appUuid, appCode, appName);
	}

	@Override
	public Sid getCompany() {
		return company;
	}

	public Long companyId() {
		return company == null ? null : company.getId();
	}

	public String companyUuid() {
		return company == null ? null : company.getUuid();
	}

	public String companyName() {
		return company == null ? null : company.getName();
	}

	public void setCompany(Sid company) {
		this.company = company;
	}

	@Override
	public Sid getOrg() {
		return org;
	}

	public Long orgId() {
		return org == null ? null : org.getId();
	}

	public String orgUuid() {
		return org == null ? null : org.getUuid();
	}

	public String orgName() {
		return org == null ? null : org.getName();
	}

	public void setOrg(Sid org) {
		this.org = org;
	}

	@Override
	public Sid getSid() {
		return sid;
	}

	public Long id() {
		return sid == null ? null : sid.getId();
	}

	public String uuid() {
		return sid == null ? null : sid.getUuid();
	}

	public String name() {
		return sid == null ? null : sid.getName();
	}

	@Override
	public void setSid(Sid id) {
		this.sid = id;
	}

	@Override
	public String getLoginName() {
		return loginName;
	}

	@Override
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public String getUserType() {
		return userType;
	}

	@Override
	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public Sid getApp() {
		return app;
	}

	public Long appId() {
		return app == null ? null : app.getId();
	}

	public String appUuid() {
		return app == null ? null : app.getUuid();
	}

	public String appCode() {
		return app == null ? null : app.getCode();
	}

	public String appName() {
		return app == null ? null : app.getName();
	}

	public void setApp(Sid app) {
		this.app = app;
	}

	public boolean isAdmin() {
		return admin || this.getLoginName().equals("superadmin");
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public Integer getLoginMode() {
		LoginMode mode = LoginMode.get(loginMode);
		if (mode != null) {
			return mode.getCode();
		}
		return LoginMode.Normal.getCode();
	}

	public void setLoginMode(Integer loginMode) {
		this.loginMode = loginMode;
	}

	@Override
	public ISimpleSessionUser getSrcUser() {
		return srcUser;
	}

	public void setSrcUser(ISimpleSessionUser srcUser) {
		this.srcUser = srcUser;
	}

	public void proxyFrom(SimpleSessionUser srcUser) {
		this.loginMode = LoginMode.Proxy.getCode();
		this.srcUser = srcUser;
	}

	public void proxyTo(SimpleSessionUser destUser) {
		destUser.loginMode = LoginMode.Proxy.getCode();
		destUser.srcUser = this;
	}

}
