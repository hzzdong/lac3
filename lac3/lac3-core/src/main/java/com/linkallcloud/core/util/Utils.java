package com.linkallcloud.core.util;

import java.security.SecureRandom;
import java.util.UUID;

import com.linkallcloud.core.lang.Strings;

public class Utils {
    
    /**
     * 往StringBuffer中append，前后不加入空格
     * 
     * @param buffer
     * @param cs
     * @param headWhiteSpace
     * @param tailWhiteSpace
     */
    public static void append(StringBuffer buffer, CharSequence cs) {
        append(buffer, cs, false, false);
    }

    /**
     * 往StringBuffer中append，前后是否加入空格
     * 
     * @param buffer
     * @param cs
     * @param headWhiteSpace
     * @param tailWhiteSpace
     */
    public static void append(StringBuffer buffer, CharSequence cs, boolean headWhiteSpace, boolean tailWhiteSpace) {
        if (null != buffer) {
            if (headWhiteSpace) {
                buffer.append(IConstants.WHITE_SPACE);
            }
            buffer.append(cs);
            if (tailWhiteSpace) {
                buffer.append(IConstants.WHITE_SPACE);
            }
        }
    }

    /**
     * 往StringBuffer中append，前后不加入空格
     * 
     * @param buffer
     * @param cs
     * @param headWhiteSpace
     * @param tailWhiteSpace
     */
    public static void append(StringBuffer buffer, char cs) {
        append(buffer, cs, false, false);
    }

    /**
     * 往StringBuffer中append，前后是否加入空格
     * 
     * @param buffer
     * @param cs
     * @param headWhiteSpace
     * @param tailWhiteSpace
     */
    public static void append(StringBuffer buffer, char cs, boolean headWhiteSpace, boolean tailWhiteSpace) {
        if (null != buffer) {
            if (headWhiteSpace) {
                buffer.append(IConstants.WHITE_SPACE);
            }
            buffer.append(cs);
            if (tailWhiteSpace) {
                buffer.append(IConstants.WHITE_SPACE);
            }
        }
    }

    /**
     * Returns a printable String corresponding to a byte array.
     * 
     * @param b
     * @return rand String
     */
    public static synchronized String printable(byte[] b) {
        final char[] alphabet =
                (IConstants.SMALL_LETTERS + IConstants.CAPITAL_LETTERS + IConstants.NUMBERS).toCharArray();
        char[] out = new char[b.length];
        for (int i = 0; i < b.length; i++) {
            int index = b[i] % alphabet.length;
            if (index < 0) {
                index += alphabet.length;
            }
            out[i] = alphabet[index];
        }
        return new String(out);
    }

    /**
     * Returns a printable String corresponding to a byte array.
     * 
     * @param b
     * @return number string
     */
    public static synchronized String printableNumber(byte[] b) {
        final char[] alphabet = IConstants.NUMBERS.toCharArray();
        char[] out = new char[b.length];
        for (int i = 0; i < b.length; i++) {
            int index = b[i] % alphabet.length;
            if (index < 0) {
                index += alphabet.length;
            }
            out[i] = alphabet[index];
        }
        return new String(out);
    }

    /**
     * get rand id
     * 
     * @param length
     * @return random id
     */
    public static String getRandomID(int length) {
        // produce the random transaction ID
        byte[] b = new byte[length];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(b);
        return Utils.printable(b);
    }

    /**
     * get numberic rand id
     * 
     * @param length
     * @return random id
     */
    public static String getNumericRandomID(int length) {
        // produce the random transaction ID
        byte[] b = new byte[length];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(b);
        return Utils.printableNumber(b);
    }

    /**
     * 
     * @param ipStr
     * @param size
     * @return ip value
     */
    public static String parse2ipVal(String ipStr, int size) {
        StringBuffer sbf = new StringBuffer();
        String[] ipArr = ipStr.replace(".", String.valueOf(IConstants.COLON)).split(String.valueOf(IConstants.COLON));
        for (String str : ipArr) {
            int length = size - str.length();
            for (int i = 0; i < length; i++) {
                sbf.append('0');
            }
            sbf.append(str);
        }
        return sbf.toString();
    }

    /**
     * 如果uuid为空或者格式无效，则返回空
     * 
     * @param uuid
     * @return
     */
    public static UUID getUUIDFromString(String uuid) {
        UUID uuidObj = null;
        if (!Strings.isBlank(uuid)) {
            try {
                uuidObj = UUID.fromString(uuid);
            } catch (Exception e) {
            }
        }
        return uuidObj;
    }
}
