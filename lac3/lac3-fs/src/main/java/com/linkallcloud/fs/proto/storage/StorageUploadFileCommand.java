package com.linkallcloud.fs.proto.storage;

import java.io.InputStream;

import com.linkallcloud.fs.domain.StorePath;
import com.linkallcloud.fs.proto.AbstractFdfsCommand;
import com.linkallcloud.fs.proto.FdfsResponse;
import com.linkallcloud.fs.proto.storage.internal.StorageUploadFileRequest;

/**
 * 文件上传命令
 * 
 */
public class StorageUploadFileCommand extends AbstractFdfsCommand<StorePath> {

    /**
     * 文件上传命令
     * 
     * @param storeIndex 存储节点
     * @param inputStream 输入流
     * @param fileExtName 文件扩展名
     * @param size 文件大小
     * @param isAppenderFile 是否支持断点续传
     */
    public StorageUploadFileCommand(byte storeIndex, InputStream inputStream, String fileExtName, long fileSize,
            boolean isAppenderFile) {
        super();
        this.request = new StorageUploadFileRequest(storeIndex, inputStream, fileExtName, fileSize, isAppenderFile);
        // 输出响应
        this.response = new FdfsResponse<StorePath>() {
            // default response
        };
    }

}
