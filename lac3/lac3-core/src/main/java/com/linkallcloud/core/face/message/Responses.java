package com.linkallcloud.core.face.message;

import com.linkallcloud.core.face.message.response.FaceResponse;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;

public class Responses {
	
	public static FaceResponse success(Object data) {
        return new ObjectFaceResponse<Object>(data);
    }

    public static FaceResponse warn(String resultCode, String msg) {
        return new FaceResponse(resultCode, msg);
    }

    public static FaceResponse warn(String resultCode) {
        return new FaceResponse(resultCode);
    }

}
