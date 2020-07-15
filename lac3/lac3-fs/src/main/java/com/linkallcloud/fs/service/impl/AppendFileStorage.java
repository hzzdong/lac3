package com.linkallcloud.fs.service.impl;

import java.io.InputStream;

import com.linkallcloud.fs.domain.StorageNodeInfo;
import com.linkallcloud.fs.domain.StorePath;
import com.linkallcloud.fs.proto.storage.StorageAppendFileCommand;
import com.linkallcloud.fs.proto.storage.StorageModifyCommand;
import com.linkallcloud.fs.proto.storage.StorageTruncateCommand;
import com.linkallcloud.fs.proto.storage.StorageUploadFileCommand;
import com.linkallcloud.fs.service.IAppendFileStorage;
import org.springframework.stereotype.Component;

import com.linkallcloud.fs.domain.StorageNode;

/**
 * 存储服务客户端接口实现
 * 
 *
 */
@Component
public class AppendFileStorage extends GenerateStorage implements IAppendFileStorage {

    /**
     * 上传支持断点续传的文件
     */
    @Override
    public StorePath uploadAppenderFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        StorageNode client = tracker.getStoreStorage(groupName);
        StorageUploadFileCommand command =
                new StorageUploadFileCommand(client.getStoreIndex(), inputStream, fileExtName, fileSize, true);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 继续上载文件
     */
    @Override
    public void appendFile(String groupName, String path, InputStream inputStream, long fileSize) {
        StorageNodeInfo client = tracker.getUpdateStorage(groupName, path);
        StorageAppendFileCommand command = new StorageAppendFileCommand(inputStream, fileSize, path);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 修改文件
     */
    @Override
    public void modifyFile(String groupName, String path, InputStream inputStream, long fileSize, long fileOffset) {
        StorageNodeInfo client = tracker.getUpdateStorage(groupName, path);
        StorageModifyCommand command = new StorageModifyCommand(path, inputStream, fileSize, fileOffset);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);

    }

    /**
     * 清除文件
     */
    @Override
    public void truncateFile(String groupName, String path, long truncatedFileSize) {
        StorageNodeInfo client = tracker.getUpdateStorage(groupName, path);
        StorageTruncateCommand command = new StorageTruncateCommand(path, truncatedFileSize);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 清除文件
     */
    @Override
    public void truncateFile(String groupName, String path) {
        long truncatedFileSize = 0;
        truncateFile(groupName, path, truncatedFileSize);
    }

}
