package com.linkallcloud.fs.proto.storage;

import com.linkallcloud.fs.proto.AbstractFdfsCommand;
import com.linkallcloud.fs.proto.FdfsResponse;
import com.linkallcloud.fs.proto.storage.internal.StorageTruncateRequest;

/**
 * 文件Truncate命令
 * 
 */
public class StorageTruncateCommand extends AbstractFdfsCommand<Void> {

    /**
     * 文件Truncate命令
     * 
     * @param groupName
     * @param path
     */
    public StorageTruncateCommand(String path, long fileSize) {
        super();
        this.request = new StorageTruncateRequest(path, fileSize);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
