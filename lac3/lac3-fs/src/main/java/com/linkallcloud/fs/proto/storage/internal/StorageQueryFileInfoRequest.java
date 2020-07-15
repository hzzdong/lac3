package com.linkallcloud.fs.proto.storage.internal;

import com.linkallcloud.fs.proto.CmdConstants;
import com.linkallcloud.fs.proto.FdfsRequest;
import com.linkallcloud.fs.proto.OtherConstants;
import com.linkallcloud.fs.proto.ProtoHead;
import com.linkallcloud.fs.proto.mapper.DynamicFieldType;
import com.linkallcloud.fs.proto.mapper.FdfsColumn;

/**
 * 查询文件信息命令
 * 
 */
public class StorageQueryFileInfoRequest extends FdfsRequest {

    /** 组名 */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 路径名 */
    @FdfsColumn(index = 1, dynamicField = DynamicFieldType.allRestByte)
    private String path;

    /**
     * 删除文件命令
     * 
     * @param groupName
     * @param path
     */
    public StorageQueryFileInfoRequest(String groupName, String path) {
        super();
        this.groupName = groupName;
        this.path = path;
        this.head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_QUERY_FILE_INFO);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
