package com.linkallcloud.core.www;

import java.io.Serializable;
import java.util.Date;

public interface ISessionUser extends Serializable {
	
	static String SESSION_USER_KEY = "_LAC_SESSION_USER_KEY_";

    String getId();
    void setId(String id);

    String getUuid();
    void setUuid(String uuid);

    String getLoginName();
    void setLoginName(String loginName);
    
    boolean isAdmin();
    void setAdmin(boolean admin);

    String getName();
    void setName(String name);

    String getUserType();
    void setUserType(String userType);

    String getNickName();
    void setNickName(String nickName);

    String getSex();
    void setSex(String sex);

    Date getBirthday();
    void setBirthday(Date birthday);

    String getIco();
    void setIco(String ico);

    String getRemark();
    void setRemark(String remark);
    
    String getCompanyId();
    void setCompanyId(String companyId);
    
    String getCompanyName();
    void setCompanyName(String companyName);
    
    String getOrgId();
    void setOrgId(String orgId);
    
    String getOrgName();
    void setOrgName(String orgName);
    
    String getOrgType();
    void setOrgType(String orgType);
    
    String getPhone();
    void setPhone(String phone);
    
    String getLoginWay();
    void setLoginWay(String way);
    
    String getAreaId();
    void setAreaId(String areaId);
    
    
    String getAreaName();
    void setAreaName(String areaName);
    
    
    int getAreaLevel();
    void setAreaLevel(int areaLevel);
    
    String getAppId();
    String getAppUuid();
    String getAppName();

}
