package com.linkallcloud.core.face.message.response;

public class ObjectFaceResponse<T> extends FaceResponse {
    private static final long serialVersionUID = 3293372681473910675L;

    private T data;

    public ObjectFaceResponse() {
        super();
    }

    public ObjectFaceResponse(T data) {
        super();
        this.data = data;
    }

    public ObjectFaceResponse(String code, String message, String versn) {
        super(code, message, versn);
    }

    public ObjectFaceResponse(String code, String message) {
        super(code, message);
    }

    public ObjectFaceResponse(String code) {
        super(code);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
