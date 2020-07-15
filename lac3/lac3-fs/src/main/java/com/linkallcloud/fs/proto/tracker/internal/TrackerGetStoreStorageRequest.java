package com.linkallcloud.fs.proto.tracker.internal;

import com.linkallcloud.fs.proto.CmdConstants;
import com.linkallcloud.fs.proto.FdfsRequest;
import com.linkallcloud.fs.proto.ProtoHead;

/**
 * 获取存储节点请求
 * 
 */
public class TrackerGetStoreStorageRequest extends FdfsRequest {

    private static final byte withoutGroupCmd = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;

    /**
     * 获取存储节点
     * 
     */
    public TrackerGetStoreStorageRequest() {
        super();
        this.head = new ProtoHead(withoutGroupCmd);
    }

}
