package com.linkallcloud.core.face.message.response;

public class BlankFaceResponse extends FaceResponse {
    private static final long serialVersionUID = -7802460937870572972L;

    public BlankFaceResponse() {
        super();
        setMessage("没有数据");
    }

    public BlankFaceResponse(String responseStatus, String responseStatusDesc, String versn) {
        super(responseStatus, responseStatusDesc, versn);
    }

    public BlankFaceResponse(String responseStatus, String responseStatusDesc) {
        super(responseStatus, responseStatusDesc);
    }

    public BlankFaceResponse(String responseStatus) {
        super(responseStatus);
    }

}
