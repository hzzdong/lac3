package com.linkallcloud.web.session;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionUser implements ISessionUser {
    private static final long serialVersionUID = -1668176164184665560L;

    private String companyId;
    private String companyName;

    private String orgId;// 操作者所属的组织ID
    private String orgName;
    private String orgType;// 操作者所属的组织类型

    private String areaId; // 所在区域
    private String areaCode;// 所在区域
    private String areaName;// 所在区域
    private int areaLevel; // 所在区域level

    private String id;
    private String uuid;
    private String loginName;
    private String name;
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
    private String appId;// 当前登录的app
    private String appUuid;
    private String appCode;
    private String appName;
    private String[] menuPermissions;
    private Long[] orgPermissions;
    private Long[] areaPermissions;

    private String orgDataSource;//数据源标识，用于多数据库情况

    public SessionUser() {
    }

    public SessionUser(String id, String uuid, String loginName, String name, String userType) {
        this.id = id;
        this.uuid = uuid;
        this.loginName = loginName;
        this.name = name;
        this.userType = userType;
    }

    public SessionUser(String id, String uuid, String loginName, String name, String userType, String companyId,
                       String companyName, String orgId, String orgName, String orgType) {
        this(id, uuid, loginName, name, userType);
        this.companyId = companyId;
        this.companyName = companyName;
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgType = orgType;
    }

    public void setInfo(String userType, String loginName, String userName, String userId, String companyId,
                        String companyName) {
        this.userType = userType;
        this.loginName = loginName;
        this.name = userName;
        this.id = userId;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    @Override
    public String getCompanyId() {
        return companyId;
    }

    @Override
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String getOrgId() {
        return orgId;
    }

    @Override
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String getOrgType() {
        return orgType;
    }

    @Override
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppInfo(String appId, String appUuid, String appCode, String appName) {
        this.appId = appId;
        this.appUuid = appUuid;
        this.appCode = appCode;
        this.appName = appName;
    }

    public void setAppInfo(String appId, String appCode) {
        this.appId = appId;
        this.appCode = appCode;
    }

    public void setPermissions(String[] menuPermissions, Long[] orgPermissions, Long[] areaPermissions) {
        this.menuPermissions = menuPermissions;
        this.orgPermissions = orgPermissions;
        this.areaPermissions = areaPermissions;
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

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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

    public void setAreaInfo(Long areaId, int level, String name) {
        this.areaId = areaId.toString();
        this.areaLevel = level;
        this.areaName = name;
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
}
