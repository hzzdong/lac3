package com.linkallcloud.core.face.message.request;

import java.util.Map;

public class RelParentIdFaceRequest extends ParentIdFaceRequest {
    private static final long serialVersionUID = 4502634098712181820L;

    private Map<String, Long> uuidIds;

    public RelParentIdFaceRequest() {
        super();
    }

    public RelParentIdFaceRequest(Long parentId, String parentClass, Long id, String uuid, Map<String, Long> uuidIds) {
        super(parentId, parentClass, id, uuid);
        this.uuidIds = uuidIds;
    }

    public Map<String, Long> getUuidIds() {
        return uuidIds;
    }

    public void setUuidIds(Map<String, Long> uuidIds) {
        this.uuidIds = uuidIds;
    }
}
