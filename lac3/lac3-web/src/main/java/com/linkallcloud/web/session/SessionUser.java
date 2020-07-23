package com.linkallcloud.web.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;

public class SessionUser extends SimpleSessionUser implements ISessionUser {
	private static final long serialVersionUID = -1668176164184665560L;

	private Sid area; // 所在区域
	private int areaLevel; // 所在区域level

	private String sex;
	private Date birthday;
	private String ico;// 头像
	private String phone;
	private String nickName;
	private String remark;// 备注

	private String loginWay;// 登录途径

	private List<Object> extendFields;

	// 以下是等到到具体某app相关权限信息
	private String[] menuPermissions;
	private Long[] orgPermissions;
	private Long[] areaPermissions;

	private List<Sid> myOrgs;// 所在机构+兼职机构列表

	private String orgDataSource;// 数据源标识，用于多数据库情况

	public SessionUser() {
		super();
	}

	public SessionUser(Long id, String uuid, String loginName, String name, String userType) {
		super(id, uuid, loginName, name, userType);
	}

	public SessionUser(Long id, String uuid, String loginName, String name, String userType, Long companyId,
			String companyUuid, String companyCode, String companyName, Long orgId, String orgUuid, String orgCode,
			String orgName) {
		super(id, uuid, loginName, name, userType, companyId, companyUuid, companyCode, companyName, orgId, orgUuid,
				orgCode, orgName);
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

	public void setPermissions(String[] menuPermissions, Long[] orgPermissions, Long[] areaPermissions) {
		this.menuPermissions = menuPermissions;
		this.orgPermissions = orgPermissions;
		this.areaPermissions = areaPermissions;
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
	public String getNickName() {
		return nickName;
	}

	@Override
	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getOrgDataSource() {
		return orgDataSource;
	}

	public void setOrgDataSource(String orgDataSource) {
		this.orgDataSource = orgDataSource;
	}

	@Override
	public List<Sid> getMyOrgs() {
		return myOrgs;
	}

	public void setMyOrgs(List<Sid> myOrgs) {
		this.myOrgs = myOrgs;
	}
}
