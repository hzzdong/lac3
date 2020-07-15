package com.linkallcloud.fs.service.impl;

import java.io.InputStream;
import java.util.Set;

import javax.annotation.Resource;

import com.linkallcloud.fs.domain.StorePath;
import com.linkallcloud.fs.proto.storage.StorageSetMetadataCommand;
import com.linkallcloud.fs.proto.storage.StorageUploadFileCommand;
import com.linkallcloud.fs.proto.storage.StorageUploadSlaveFileCommand;
import com.linkallcloud.fs.proto.storage.enums.StorageMetdataSetType;
import com.linkallcloud.fs.service.IFastFileStorage;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import com.linkallcloud.fs.domain.MateData;
import com.linkallcloud.fs.domain.StorageNode;
import com.linkallcloud.fs.domain.ThumbImageConfig;

/**
 * 面向应用的接口实现
 * 
 *
 */
@Component
public class FastFileStorage extends GenerateStorage implements IFastFileStorage {

    @Resource
    private ThumbImageConfig thumbImageConfig;

    /**
     * 上传文件
     */
    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.isTrue(fileSize > 0, "文件内容为空.");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");

        StorageNode client = tracker.getStoreStorage();
        // 上传文件
        StorageUploadFileCommand command =
                new StorageUploadFileCommand(client.getStoreIndex(), inputStream, fileExtName, fileSize, false);
        StorePath path = connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
        // 上传matadata
        if (hasMateData(metaDataSet)) {
            StorageSetMetadataCommand setMDCommand = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                    metaDataSet, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
            connectionManager.executeFdfsCmd(client.getInetSocketAddress(), setMDCommand);
        }
        return path;
    }

    @Override
    public StorePath uploadSlaveFile(String masterFilename, InputStream inputStream, long fileSize, String prefixName,
            String fileExtName, Set<MateData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.isTrue(fileSize > 0, "文件内容为空.");
        Validate.notBlank(masterFilename, "masterFilename不能为空");
        Validate.notBlank(prefixName, "prefixName不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");

        StorageNode client = tracker.getStoreStorage();
        // 上传文件
        StorageUploadSlaveFileCommand command =
                new StorageUploadSlaveFileCommand(inputStream, fileSize, masterFilename, prefixName, fileExtName);
        StorePath path = connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
        // 上传matadata
        if (hasMateData(metaDataSet)) {
            StorageSetMetadataCommand setMDCommand = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                    metaDataSet, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
            connectionManager.executeFdfsCmd(client.getInetSocketAddress(), setMDCommand);
        }
        return path;
    }

    /**
     * 检查是否有MateData
     * 
     * @param metaDataSet
     * @return
     */
    private boolean hasMateData(Set<MateData> metaDataSet) {
        return null != metaDataSet && !metaDataSet.isEmpty();
    }

    /**
     * 删除文件
     */
    @Override
    public void deleteFile(String filePath) {
        StorePath storePath = StorePath.praseFromUrl(filePath);
        super.deleteFile(storePath.getGroup(), storePath.getPath());
    }

}
