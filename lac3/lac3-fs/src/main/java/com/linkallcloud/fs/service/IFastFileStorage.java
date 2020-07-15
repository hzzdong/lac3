package com.linkallcloud.fs.service;

import java.io.InputStream;
import java.util.Set;

import com.linkallcloud.fs.domain.MateData;
import com.linkallcloud.fs.domain.StorePath;

/**
 * 面向普通应用的文件操作接口封装
 * 
 */
public interface IFastFileStorage extends IGenerateStorage {

    /**
     * 上传一般文件
     * 
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @param metaDataSet
     * @return
     */
    StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MateData> metaDataSet);

    /**
     * 上传从文件
     * 
     * @param masterFilename
     * @param inputStream
     * @param fileSize
     * @param prefixName
     * @param fileExtName
     * @param metaDataSet
     * @return
     */
    StorePath uploadSlaveFile(String masterFilename, InputStream inputStream, long fileSize, String prefixName,
            String fileExtName, Set<MateData> metaDataSet);

    /**
     * 删除文件
     * 
     * @param filePath
     *            文件路径(groupName/path)
     */
    void deleteFile(String filePath);

}
