package com.linkallcloud.fs.proto.tracker;

import java.util.List;

import com.linkallcloud.fs.domain.StorageState;
import com.linkallcloud.fs.proto.AbstractFdfsCommand;
import com.linkallcloud.fs.proto.tracker.internal.TrackerListStoragesRequest;
import com.linkallcloud.fs.proto.tracker.internal.TrackerListStoragesResponse;

/**
 * 列出组命令
 * 
 */
public class TrackerListStoragesCommand extends AbstractFdfsCommand<List<StorageState>> {

    public TrackerListStoragesCommand(String groupName, String storageIpAddr) {
        super.request = new TrackerListStoragesRequest(groupName, storageIpAddr);
        super.response = new TrackerListStoragesResponse();
    }

    public TrackerListStoragesCommand(String groupName) {
        super.request = new TrackerListStoragesRequest(groupName);
        super.response = new TrackerListStoragesResponse();
    }

}
