package com.linkallcloud.core.security;

import com.linkallcloud.core.lang.Lang;

public class Securities {

    /**
     * 
     * @param md5rc
     * @param salt
     * @return
     */
    public static String password4Md5Src(String md5rc, String salt) {
        return Lang.sha1(md5rc + salt);
    }

    /**
     * 
     * @param src
     * @param salt
     * @return
     */
    public static String password4Src(String src, String salt) {
        return Securities.password4Md5Src(Lang.md5(src), salt);
    }

    /**
     * 
     * @param md5rc
     * @param salt
     * @param password
     * @return
     */
    public static boolean validePassword4Md5Src(String md5rc, String salt, String password) {
        return Securities.password4Md5Src(md5rc, salt).equals(password);
    }

    /**
     * 
     * @param src
     * @param salt
     * @param password
     * @return
     */
    public static boolean validePassword4Src(String src, String salt, String password) {
        return Securities.validePassword4Md5Src(Lang.md5(src), salt, password);
    }

    public static String password4PBKDF2(String plainPassword) {
        PBKDF2 pbkdf2Factory = new PBKDF2();
        return pbkdf2Factory.createPassword(plainPassword);
    }

    public static boolean validePassword4PBKDF2(String plainPassword, String hashedPasword) {
        PBKDF2 pbkdf2Factory = new PBKDF2();
        return pbkdf2Factory.isValidPassword(plainPassword, hashedPasword);
    }

}
