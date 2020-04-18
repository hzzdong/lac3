package com.linkallcloud.core.www;

import com.linkallcloud.core.dto.Sid;

import java.io.Serializable;
import java.util.Date;

public interface ISessionUser extends Serializable {
	
	static String SESSION_USER_KEY = "_LAC_SESSION_USER_KEY_";

    Sid getSid();
    void setSid(Sid id);

    String getLoginName();
    void setLoginName(String loginName);
    
    boolean isAdmin();
    void setAdmin(boolean admin);

    String getUserType();
    void setUserType(String userType);

    String getNickName();
    void setNickName(String nickName);

    String getPhone();
    void setPhone(String phone);

    String getSex();
    void setSex(String sex);

    Date getBirthday();
    void setBirthday(Date birthday);

    String getIco();
    void setIco(String ico);

    String getRemark();
    void setRemark(String remark);

    String getLoginWay();
    void setLoginWay(String way);

    Sid getCompany();
    Sid getOrg();
    Sid getArea();
    int getAreaLevel();
    Sid getApp();

}
