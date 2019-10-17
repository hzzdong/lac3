package com.linkallcloud.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


/**
 * 功能：
 * 
 * 1 、实现把指定文件夹下的所有文件压缩为指定文件夹下指定 zip 文件
 * 
 * 2 、实现把指定文件夹下的 zip 文件解压到指定目录下
 * 
 */
public class ZipUtils {
    private static Log log = Logs.getLog(ZipUtils.class);
    private static final int BUFFER_LENGTH = 1024;

    /**
     * 
     * 功能：把 sourceDir 目录下的所有文件进行 zip 格式的压缩，保存为指定 zip 文件
     * 
     * @param sourceDir
     * @param zipFile
     * 
     *            格式： E:\\stu \\zipFile.zip 注意：加入 zipFile 我们传入的字符串值是 ： "E:\\stu \\" 或者 "E:\\stu " 如果 E 盘已经存在 stu
     *            这个文件夹的话，那么就会出现 java.io.FileNotFoundException: E:\stu ( 拒绝访问。 ) 这个异常，所以要注意正确传参调用本函数哦
     */

    public static void zip(String sourceDir, String zipFile) {
        OutputStream os;
        try {
            os = new FileOutputStream(zipFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            ZipOutputStream zos = new ZipOutputStream(bos);
            File file = new File(sourceDir);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
            zos.closeEntry();
            zos.close();
            log.info("文件压缩成功. ");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 
     * @param source
     * @param basePath
     * @param zos
     */
    private static void zipFile(File source, String basePath, ZipOutputStream zos) {
        File[] files = new File[0];
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;
        byte[] buf = new byte[BUFFER_LENGTH];
        int length = 0;
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    pathName = file.getPath().substring(basePath.length() + 1) + "/";
                    zos.putNextEntry(new ZipEntry(pathName));
                    zipFile(file, basePath, zos);
                } else {
                    pathName = file.getPath().substring(basePath.length() + 1);
                    InputStream is = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    zos.putNextEntry(new ZipEntry(pathName));
                    while ((length = bis.read(buf)) > 0) {
                        zos.write(buf, 0, length);
                    }
                    is.close();
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 解压 zip 文件，注意不能解压 rar 文件哦，只能解压 zip
     * 
     * @param zipfile
     * @param destDir
     */
    public static void unZip(File zipfile, String destDir) {
        unZip(zipfile, destDir, IConstants.ENCODING_GBK);
    }

    /**
     * 解压 zip 文件，注意不能解压 rar 文件哦，只能解压 zip
     * 
     * @param zipfile
     * @param destDir
     */
    public static void unZip(String zipfile, String destDir) {
        unZip(zipfile, destDir, IConstants.ENCODING_GBK);
    }

    /**
     * 解压 zip 文件，注意不能解压 rar 文件哦，只能解压 zip
     * 
     * @param zipfile
     * @param destDir
     * @param fileNameEncoding
     */
    public static void unZip(String zipfile, String destDir, String fileNameEncoding) {
        File f = new File(zipfile);
        if (null != f) {
            unZip(f, destDir, fileNameEncoding);
        }
    }

    /**
     * 解压 zip 文件，注意不能解压 rar 文件哦，只能解压 zip
     * 
     * @param zipfile
     * @param destDir
     * @param fileNameEncoding
     */
    @SuppressWarnings("rawtypes")
    public static void unZip(File zipfile, String destDir, String fileNameEncoding) {
        String dir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        byte[] b = new byte[BUFFER_LENGTH];
        int length;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(zipfile, fileNameEncoding);
            Enumeration enumeration = zipFile.getEntries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                File loadFile = new File(dir + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    // 这段都可以不要，因为每次都貌似从最底层开始遍历的
                    loadFile.mkdirs();
                } else {
                    if (!loadFile.getParentFile().exists()) {
                        loadFile.getParentFile().mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(loadFile);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    while ((length = inputStream.read(b)) > 0) {
                        outputStream.write(b, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();
                }
            }
            log.info("文件解压成功. ");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
