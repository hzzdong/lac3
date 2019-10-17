package com.linkallcloud.core.face.message.response;

public class ErrorFaceResponse extends FaceResponse {
    private static final long serialVersionUID = -2631194640721838249L;

    public ErrorFaceResponse(String code, String message, String versn) {
        super(code, message, versn);
    }

    public ErrorFaceResponse(String code, String message) {
        super(code, message);
    }

    public ErrorFaceResponse(String code) {
        super(code);
    }

    public ErrorFaceResponse() {
        super();
    }

}
