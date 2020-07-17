package com.linkallcloud.core.www;

import java.util.Date;
import java.util.List;

import com.linkallcloud.core.dto.Sid;

public interface ISessionUser extends ISimpleSessionUser {
	
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
    
    Sid getArea();
    int getAreaLevel();
    
    List<Sid> getMyOrgs();

}
