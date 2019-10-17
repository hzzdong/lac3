package com.linkallcloud.core.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.linkallcloud.core.lang.Strings;
import org.apache.commons.codec.binary.Base64;

import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {

    /** 支持的图片类型 */
    private static final String[] IMAGE_TYPE = { "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP" };
    private static final List<String> IMAGE_LIST = Arrays.asList(IMAGE_TYPE);

    public static ByteArrayOutputStream imageResize(InputStream inputStream, int width, int height) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).size(width, height).toOutputStream(out);
        return out;
    }

    public static ByteArrayOutputStream imageCut(InputStream inputStream, int x, int y, int width, int height)
            throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).sourceRegion(x, y, width, height).toOutputStream(out);
        return out;
    }

    public static ByteArrayOutputStream imageCutResize(InputStream inputStream, int x, int y, int width, int height,
            int resizeWidth, int resizeHeight) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).sourceRegion(x, y, width, height).size(resizeWidth, resizeHeight)
                .toOutputStream(out);
        return out;
    }

    public static boolean isSupportImage(String fileExtName) {
        return IMAGE_LIST.contains(fileExtName.toUpperCase());
    }

    /**
     * 解析出文件扩展名
     * 
     * @param fileName
     * @return
     */
    public static String parseFileExtName(String fileName) {
        if (!Strings.isBlank(fileName)) {
            int idx = fileName.lastIndexOf(".");
            if (idx != -1) {
                return fileName.substring(idx + 1);
            }
        }
        return null;
    }

    /**
     * 解析出文件扩展名，带.号
     * 
     * @param fileName
     * @return
     */
    public static String parseFileExt(String fileName) {
        if (!Strings.isBlank(fileName)) {
            int idx = fileName.lastIndexOf(".");
            if (idx != -1) {
                return fileName.substring(idx);
            }
        }
        return null;
    }

    /**
     * 判断是否数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据图片地址转换为base64编码字符串
     * 
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 将base64编码字符串转换为图片
     * 
     * @param imgStr
     *            base64编码字符串
     * @param path
     *            图片路径-具体到文件
     * @return
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null) {
            return false;
        }
        try {
            byte[] b = Base64.decodeBase64(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
