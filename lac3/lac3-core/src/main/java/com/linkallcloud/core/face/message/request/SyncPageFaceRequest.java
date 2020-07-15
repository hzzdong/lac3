package com.linkallcloud.core.face.message.request;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 分页同步
 *
 */
public class SyncPageFaceRequest extends PageFaceRequest {
    private static final long serialVersionUID = 3529630103186260279L;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastSyncTime;

    public SyncPageFaceRequest() {
        super();
    }

    public SyncPageFaceRequest(int start, int length, String sessionId, String versn) {
        super(start, length, sessionId, versn);
    }

    public SyncPageFaceRequest(int start, int length) {
        super(start, length);
    }

    public SyncPageFaceRequest(String sessionId, String versn) {
        super(sessionId, versn);
    }

    public SyncPageFaceRequest(String sessionId) {
        super(sessionId);
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

}
