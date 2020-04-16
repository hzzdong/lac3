package com.linkallcloud.core.face.message.request;

public class ParentIdFaceRequest extends IdFaceRequest {
    private static final long serialVersionUID = 2891217131827346766L;

    private String parentId;
    private String parentUuid;
    private String parentClass;

    public ParentIdFaceRequest() {
        super();
    }

    public ParentIdFaceRequest(String parentId, String parentClass, String id, String uuid) {
        super(id, uuid);
        this.parentId = parentId;
        this.parentClass = parentClass;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }
}
