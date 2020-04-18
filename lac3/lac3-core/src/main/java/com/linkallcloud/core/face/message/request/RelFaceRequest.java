package com.linkallcloud.core.face.message.request;

import java.util.Map;

public class RelFaceRequest extends IdFaceRequest {
    private static final long serialVersionUID = 8221378053761521557L;

    private Map<String, Long> uuidIds;

    public RelFaceRequest() {
        super();
    }

    public RelFaceRequest(Long id, String uuid, Map<String, Long> uuidIds) {
        super(id, uuid);
        this.uuidIds = uuidIds;
    }

    public Map<String, Long> getUuidIds() {
        return uuidIds;
    }

    public void setUuidIds(Map<String, Long> uuidIds) {
        this.uuidIds = uuidIds;
    }
}
