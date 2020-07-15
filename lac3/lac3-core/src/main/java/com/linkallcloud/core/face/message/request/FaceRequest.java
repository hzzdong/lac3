package com.linkallcloud.core.face.message.request;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.FaceMessage;
import com.linkallcloud.core.face.message.IRequestFaceMessage;

/**
 * 由于提供给客户前期文档中有sessionId和token两种叫法，所以本类中进行了处理，接口可以传递两者任意一个。
 */
public abstract class FaceRequest extends FaceMessage implements IRequestFaceMessage {
    private static final long serialVersionUID = -7349179477062435809L;

    private String token;
    
    private Trace t;

    public FaceRequest() {
        super();
    }

    public FaceRequest(String token, String versn) {
        super(versn);
        this.token = token;
    }

    public FaceRequest(String token) {
        super();
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Trace getT() {
        return t;
    }

    public void setT(Trace t) {
        this.t = t;
    }

}
