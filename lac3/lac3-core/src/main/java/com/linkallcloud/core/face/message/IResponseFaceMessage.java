package com.linkallcloud.core.face.message;

public interface IResponseFaceMessage extends IFaceMessage {
    
    String getCode();//返回状态码
    
    String getMessage();//返回状态说明

}
