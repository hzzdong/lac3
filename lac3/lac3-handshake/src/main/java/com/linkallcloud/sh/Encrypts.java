package com.linkallcloud.sh;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*-
 * 编码工具类
 * 1.将byte[]转为各种进制的字符串
 * 2.base 64 encode
 * 3.base 64 decode
 * 4.获取byte[]的md5/sha1值
 * 5.获取字符串md5/sha1值
 * 6.结合base64实现md5/sha1加密
 * 7.AES加密
 * 8.AES加密为base 64 code
 * 9.AES解密
 * 10.将base 64 code AES解密
 *
 */
public class Encrypts {

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes
     *            byte[]
     * @param radix
     *            可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * base 64 encode
     *
     * @param bytes
     *            待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code
     *            待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return (base64Code == null || base64Code.isEmpty()) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * 获取byte[]的md5值
     *
     * @param bytes
     *            byte[]
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        return md.digest();
    }

    /**
     * 获取字符串md5值
     *
     * @param msg
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(String msg) throws Exception {
        return (msg == null || msg.isEmpty()) ? null : md5(msg.getBytes("UTF-8"));
    }

    /**
     * 结合base64实现md5加密
     *
     * @param msg
     *            待加密字符串
     * @return 获取md5后转为base64
     * @throws Exception
     */
    public static String md5Encrypt(String msg) throws Exception {
        return (msg == null || msg.isEmpty()) ? null : base64Encode(md5(msg));
    }
    
    /**
     * 获取byte[]的SHA1值
     *
     * @param bytes
     *            byte[]
     * @return SHA1
     * @throws Exception
     */
    public static byte[] sha1(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(bytes);
        return md.digest();
    }
    
    /**
     * 获取字符串SHA1值
     *
     * @param msg
     * @return SHA1
     * @throws Exception
     */
    public static byte[] sha1(String msg) throws Exception {
        return (msg == null || msg.isEmpty()) ? null : sha1(msg.getBytes("UTF-8"));
    }
    
    /**
     * 结合base64实现SHA1加密
     *
     * @param msg
     *            待加密字符串
     * @return 获取SHA1后转为base64
     * @throws Exception
     */
    public static String sha1Encrypt(String msg) throws Exception {
        return (msg == null || msg.isEmpty()) ? null : base64Encode(sha1(msg));
    }

    /**
     * AES加密
     *
     * @param content
     *            待加密的内容
     * @param encryptKey
     *            加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        // KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // kgen.init(128, new SecureRandom(encryptKey.getBytes()));
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptKey.getBytes());
        kgen.init(128, random);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES加密为base 64 code
     *
     * @param content
     *            待加密的内容
     * @param encryptKey
     *            加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     *
     * @param encryptBytes
     *            待解密的byte[]
     * @param decryptKey
     *            解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        // KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // kgen.init(128, new SecureRandom(decryptKey.getBytes()));
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(decryptKey.getBytes());
        kgen.init(128, random);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr
     *            待解密的base 64 code
     * @param decryptKey
     *            解密密钥
     * @return 解密后的string
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return (encryptStr == null || encryptStr.isEmpty()) ? null
                : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    private static final String IV_STRING = "1234561234567890";

    /**
     * @param content
     * @param key
     * @return
     * @throws Exception
     *
     *             加密
     */
    public static String encryptAES(String content, String key) throws Exception {
        byte[] byteContent = content.getBytes("UTF-8");
        // 注意，为了能与 iOS 统一
        // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(byteContent);
        // 同样对加密后数据进行 base64 编码
        return base64Encode(encryptedBytes);
    }

    /**
     * @param content
     * @param key
     * @return
     * @throws Exception
     *
     *             解密
     */
    public static String decryptAES(String content, String key) throws Exception {
        // base64 解码
        byte[] encryptedBytes = base64Decode(content);
        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] result = cipher.doFinal(encryptedBytes);
        return new String(result, "UTF-8");
    }
    
    public static void main(String[] args) throws Exception {
        String a = "Zhoudong1234567890";
        System.out.println("MD5==========================");
        System.out.println(Encrypts.md5Encrypt(a));
        
        System.out.println("SHA1==========================");
        System.out.println(Encrypts.sha1Encrypt(a));
        
        String key =   "sdklIWDJow9q0J(W1234561234567890";
        //String key =    "6$Tur4lJtO!XH@hA";
        //String offset = "1234561234567890";
        //String offset = "R$jzD1J7eK!Ptexk";
        System.out.println("AES==========================");
        //System.out.println(AESEncryptUtils.aesEncrypt(a, key));
        String ea = Encrypts.encryptAES(a, key);
        System.out.println(ea);
        System.out.println(Encrypts.decryptAES(ea, key));
    }

}
