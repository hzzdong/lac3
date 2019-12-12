package com.linkallcloud.core.util;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.UUID;

import com.linkallcloud.core.lang.Strings;

public class Utils {
    
    /**
     * 往StringBuffer中append，前后不加入空格
     * 
     * @param buffer
     * @param cs
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

    /**
     * Check whether the object is null or not. If it is, throw an exception and display the message.
     *
     * @param object
     *            the object to check.
     * @param message
     *            the message to display if the object is null.
     */
    public static void assertNotNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check whether the collection is null or empty. If it is, throw an exception and display the message.
     *
     * @param c
     *            the collecion to check.
     * @param message
     *            the message to display if the object is null.
     */
    public static void assertNotEmpty(final Collection<?> c, final String message) {
        assertNotNull(c, message);
        if (c.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that the statement is true, otherwise throw an exception with the provided message.
     *
     * @param cond
     *            the codition to assert is true.
     * @param message
     *            the message to display if the condition is not true.
     */
    public static void assertTrue(final boolean cond, final String message) {
        if (!cond) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Determines whether the String is null or of length 0.
     *
     * @param string
     *            the string to check
     * @return true if its null or length of 0, false otherwise.
     */
    public static boolean isEmpty(final String string) {
        return string == null || string.length() == 0;
    }

    /**
     * Determines if the String is not empty. A string is not empty if it is not null and has a length > 0.
     *
     * @param string
     *            the string to check
     * @return true if it is not empty, false otherwise.
     */
    public static boolean isNotEmpty(final String string) {
        return !isEmpty(string);
    }

    /**
     * Determines if a String is blank or not. A String is blank if its empty or if it only contains spaces.
     *
     * @param string
     *            the string to check
     * @return true if its blank, false otherwise.
     */
    public static boolean isBlank(final String string) {
        return isEmpty(string) || string.trim().length() == 0;
    }

    /**
     * Determines if a string is not blank. A string is not blank if it contains at least one non-whitespace character.
     *
     * @param string
     *            the string to check.
     * @return true if its not blank, false otherwise.
     */
    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }
}
