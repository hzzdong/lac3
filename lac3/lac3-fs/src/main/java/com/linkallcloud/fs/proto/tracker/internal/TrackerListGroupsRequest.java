package com.linkallcloud.fs.proto.tracker.internal;

import com.linkallcloud.fs.proto.CmdConstants;
import com.linkallcloud.fs.proto.FdfsRequest;
import com.linkallcloud.fs.proto.ProtoHead;

/**
 * 列出分组命令
 * 
 */
public class TrackerListGroupsRequest extends FdfsRequest {

    public TrackerListGroupsRequest() {
        head = new ProtoHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
    }
}
