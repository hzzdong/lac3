package com.linkallcloud.web.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.enums.LoginMode;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;

public class SessionUser implements ISessionUser {
	private static final long serialVersionUID = -1668176164184665560L;

	private Integer loginMode;// 登录模式，normal:普通模式,proxy:代维模式
	private SessionUser srcUser;

	private Sid company;
	private Sid org;// 操作者所属的组织ID

	private Sid area; // 所在区域
	private int areaLevel; // 所在区域level

	private Sid sid;
	private String loginName;
	private String nickName;
	private String userType;// 用户类型
	private boolean admin;// 是否管理员
	private String sex;
	private Date birthday;
	private String ico;// 头像
	private String remark;// 备注
	private String phone;

	private String loginWay;// 登录途径

	private List<Object> extendFields;

	// 以下是等到到具体某app相关权限信息
	private Sid app;// 当前登录的app
	private String[] menuPermissions;
	private Long[] orgPermissions;
	private Long[] areaPermissions;

	private String orgDataSource;// 数据源标识，用于多数据库情况

	public SessionUser() {
	}

	public SessionUser(Long id, String uuid, String loginName, String name, String userType) {
		this.sid = new Sid(id, uuid, loginName, name);
		this.loginName = loginName;
		this.userType = userType;
	}

	public SessionUser(Long id, String uuid, String loginName, String name, String userType, Long companyId,
			String companyUuid, String companyName, Long orgId, String orgUuid, String orgName) {
		this(id, uuid, loginName, name, userType);
		this.company = new Sid(companyId, companyUuid, companyName);
		this.org = new Sid(orgId, orgUuid, orgName);
	}

	public void setInfo(String userType, String loginName, String userName, String userUuid, Long userId,
			Long companyId, String companyUuid, String companyName) {
		this.userType = userType;
		this.loginName = loginName;
		if (this.sid == null) {
			this.sid = new Sid(userId, userUuid, userName);
		}
		this.company = new Sid(companyId, companyUuid, companyName);
	}

	public List<Object> getExtendFields() {
		return extendFields;
	}

	public void setExtendFields(List<Object> extendFields) {
		this.extendFields = extendFields;
	}

	public void addExtendField(Object fieldObj) {
		if (this.extendFields == null) {
			this.extendFields = new ArrayList<Object>();
		}
		this.extendFields.add(fieldObj);
	}

	public void setAppInfo(Long appId, String appUuid, String appCode, String appName) {
		this.app = new Sid(appId, appUuid, appCode, appName);
	}

	public void setPermissions(String[] menuPermissions, Long[] orgPermissions, Long[] areaPermissions) {
		this.menuPermissions = menuPermissions;
		this.orgPermissions = orgPermissions;
		this.areaPermissions = areaPermissions;
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
	public Sid getArea() {
		return area;
	}

	public Long areaId() {
		return area == null ? null : area.getId();
	}

	public String areaUuid() {
		return area == null ? null : area.getUuid();
	}

	public String areaCode() {
		return area == null ? null : area.getCode();
	}

	public String areaName() {
		return area == null ? null : area.getName();
	}

	public void setArea(Sid area) {
		this.area = area;
	}

	@Override
	public int getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(int areaLevel) {
		this.areaLevel = areaLevel;
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
	public String getNickName() {
		return nickName;
	}

	@Override
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public String getSex() {
		return sex;
	}

	@Override
	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public Date getBirthday() {
		return birthday;
	}

	@Override
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String getIco() {
		return ico;
	}

	@Override
	public void setIco(String ico) {
		this.ico = ico;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String getLoginWay() {
		return loginWay;
	}

	@Override
	public void setLoginWay(String loginWay) {
		this.loginWay = loginWay;
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

	public String[] getMenuPermissions() {
		return menuPermissions;
	}

	public void setMenuPermissions(String[] menuPermissions) {
		this.menuPermissions = menuPermissions;
	}

	public Long[] getOrgPermissions() {
		return orgPermissions;
	}

	public void setOrgPermissions(Long[] orgPermissions) {
		this.orgPermissions = orgPermissions;
	}

	public Long[] getAreaPermissions() {
		return areaPermissions;
	}

	public void setAreaPermissions(Long[] areaPermissions) {
		this.areaPermissions = areaPermissions;
	}

	public boolean hasMenuPermission(String res) {
		if (Strings.isBlank(res) || this.menuPermissions == null || this.menuPermissions.length <= 0) {
			return false;
		}
		for (String resCode : menuPermissions) {
			if (res.equals(resCode)) {
				return true;
			}
		}

		return false;
	}

	public void setAreaInfo(Long areaId, String uuid, String code, String name, int level) {
		this.area = new Sid(areaId, uuid, code, name);
		this.areaLevel = level;
	}

	public boolean isAdmin() {
		return admin || this.getLoginName().equals("superadmin");
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getOrgDataSource() {
		return orgDataSource;
	}

	public void setOrgDataSource(String orgDataSource) {
		this.orgDataSource = orgDataSource;
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

	public SessionUser getSrcUser() {
		return srcUser;
	}

	public void setSrcUser(SessionUser srcUser) {
		this.srcUser = srcUser;
	}

	public void proxyFrom(SessionUser srcUser) {
		this.loginMode = LoginMode.Proxy.getCode();
		this.srcUser = srcUser;
	}

	public void proxyTo(SessionUser destUser) {
		destUser.loginMode = LoginMode.Proxy.getCode();
		destUser.srcUser = this;
	}
}
