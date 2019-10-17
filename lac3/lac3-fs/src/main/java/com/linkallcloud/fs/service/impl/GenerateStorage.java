package com.linkallcloud.fs.service.impl;

import java.io.InputStream;
import java.util.Set;

import javax.annotation.Resource;

import com.linkallcloud.fs.conn.ConnectionManager;
import com.linkallcloud.fs.proto.storage.enums.StorageMetdataSetType;
import com.linkallcloud.fs.service.IGenerateStorage;
import com.linkallcloud.fs.service.ITracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.linkallcloud.fs.domain.FileInfo;
import com.linkallcloud.fs.domain.MateData;
import com.linkallcloud.fs.domain.StorageNode;
import com.linkallcloud.fs.domain.StorageNodeInfo;
import com.linkallcloud.fs.domain.StorePath;
import com.linkallcloud.fs.proto.storage.DownloadCallback;
import com.linkallcloud.fs.proto.storage.StorageDeleteFileCommand;
import com.linkallcloud.fs.proto.storage.StorageDownloadCommand;
import com.linkallcloud.fs.proto.storage.StorageGetMetadataCommand;
import com.linkallcloud.fs.proto.storage.StorageQueryFileInfoCommand;
import com.linkallcloud.fs.proto.storage.StorageSetMetadataCommand;
import com.linkallcloud.fs.proto.storage.StorageUploadFileCommand;
import com.linkallcloud.fs.proto.storage.StorageUploadSlaveFileCommand;

/**
 * 基本存储客户端操作实现
 * 
 */
@Component
public class GenerateStorage implements IGenerateStorage {

    /** tracker */
    @Resource
    protected ITracker tracker;

    /** connectManager */
    @Resource
    protected ConnectionManager connectionManager;

    /** 日志 */
    protected static Logger LOGGER = LoggerFactory.getLogger(GenerateStorage.class);

    /**
     * 上传不支持断点续传的文件
     */
    @Override
    public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize, String fileExtName) {
        StorageNode client = tracker.getStoreStorage(groupName);
        StorageUploadFileCommand command =
                new StorageUploadFileCommand(client.getStoreIndex(), inputStream, fileExtName, fileSize, false);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 上传从文件
     */
    @Override
    public StorePath uploadSlaveFile(String groupName, String masterFilename, InputStream inputStream, long fileSize,
            String prefixName, String fileExtName) {
        StorageNodeInfo client = tracker.getUpdateStorage(groupName, masterFilename);
        StorageUploadSlaveFileCommand command =
                new StorageUploadSlaveFileCommand(inputStream, fileSize, masterFilename, prefixName, fileExtName);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 获取metadata
     */
    @Override
    public Set<MateData> getMetadata(String groupName, String path) {
        StorageNodeInfo client = tracker.getFetchStorage(groupName, path);
        StorageGetMetadataCommand command = new StorageGetMetadataCommand(groupName, path);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 覆盖metadata
     */
    @Override
    public void overwriteMetadata(String groupName, String path, Set<MateData> metaDataSet) {
        StorageNodeInfo client = tracker.getUpdateStorage(groupName, path);
        StorageSetMetadataCommand command = new StorageSetMetadataCommand(groupName, path, metaDataSet,
                StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 合并metadata
     */
    @Override
    public void mergeMetadata(String groupName, String path, Set<MateData> metaDataSet) {
        StorageNodeInfo client = tracker.getUpdateStorage(groupName, path);
        StorageSetMetadataCommand command = new StorageSetMetadataCommand(groupName, path, metaDataSet,
                StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 查询文件信息
     */
    @Override
    public FileInfo queryFileInfo(String groupName, String path) {
        StorageNodeInfo client = tracker.getFetchStorage(groupName, path);
        StorageQueryFileInfoCommand command = new StorageQueryFileInfoCommand(groupName, path);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 删除文件
     */
    @Override
    public void deleteFile(String groupName, String path) {
        StorageNodeInfo client = tracker.getUpdateStorage(groupName, path);
        StorageDeleteFileCommand command = new StorageDeleteFileCommand(groupName, path);
        connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    /**
     * 下载整个文件
     */
    @Override
    public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
        long fileOffset = 0;
        long fileSize = 0;
        return downloadFile(groupName, path, fileOffset, fileSize, callback);
    }

    /**
     * 下载文件片段
     */
    @Override
    public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize,
            DownloadCallback<T> callback) {
        StorageNodeInfo client = tracker.getFetchStorage(groupName, path);
        StorageDownloadCommand<T> command = new StorageDownloadCommand<T>(groupName, path, 0, 0, callback);
        return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    }

    public void setTracker(ITracker tracker) {
        this.tracker = tracker;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

}
