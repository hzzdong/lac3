package com.linkallcloud.fs.proto.tracker;

import java.util.List;

import com.linkallcloud.fs.domain.GroupState;
import com.linkallcloud.fs.proto.AbstractFdfsCommand;
import com.linkallcloud.fs.proto.tracker.internal.TrackerListGroupsRequest;
import com.linkallcloud.fs.proto.tracker.internal.TrackerListGroupsResponse;

/**
 * 列出组命令
 * 
 */
public class TrackerListGroupsCommand extends AbstractFdfsCommand<List<GroupState>> {

    public TrackerListGroupsCommand() {
        super.request = new TrackerListGroupsRequest();
        super.response = new TrackerListGroupsResponse();
    }

}
