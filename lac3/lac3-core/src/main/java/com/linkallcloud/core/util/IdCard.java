package com.linkallcloud.core.util;

import java.util.Calendar;
import java.util.Date;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.exception.BaseRuntimeException;

public class IdCard {

    /** 中国公民身份证号码最小长度。 */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /** 中国公民身份证号码最大长度。 */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 根据身份编号获取年龄
     *
     * @param idCard
     *            身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        if (Strings.isBlank(idCard)) {
            throw new BaseRuntimeException("10000001", "身份证号不能为空");
        }

        int iAge = 0;
        Calendar cal = Calendar.getInstance();

        String year = idCard.substring(6, 10);
        if (IdCard.CHINA_ID_MAX_LENGTH == idCard.length()) {
            year = idCard.substring(6, 10);
        } else if (IdCard.CHINA_ID_MIN_LENGTH == idCard.length()) {
            year = "19" + idCard.substring(6, 8);
        } else {
            throw new BaseRuntimeException("10000001", "身份证号只能是18或者15位");
        }

        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard
     *            身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        if (Strings.isBlank(idCard)) {
            throw new BaseRuntimeException("10000001", "身份证号不能为空");
        }

        if (IdCard.CHINA_ID_MAX_LENGTH == idCard.length()) {
            return idCard.substring(6, 14);
        } else if (IdCard.CHINA_ID_MIN_LENGTH == idCard.length()) {
            return "19" + idCard.substring(6, 12);
        } else {
            throw new BaseRuntimeException("10000001", "身份证号只能是18或者15位");
        }
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard
     *            身份编号
     * @return 生日(yyyy-MM-dd)
     */
    public static Date getBirthdayByIdCard(String idCard) {
        if (Strings.isBlank(idCard)) {
            throw new BaseRuntimeException("10000001", "身份证号不能为空");
        }

        String birthday = null;
        if (IdCard.CHINA_ID_MAX_LENGTH == idCard.length()) {
            birthday = idCard.substring(6, 14);
        } else if (IdCard.CHINA_ID_MIN_LENGTH == idCard.length()) {
            birthday = "19" + idCard.substring(6, 12);
        } else {
            throw new BaseRuntimeException("10000001", "身份证号只能是18或者15位");
        }
        return Dates.parseDate(birthday, "yyyyMMdd");
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard
     *            身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        if (Strings.isBlank(idCard)) {
            throw new BaseRuntimeException("10000001", "身份证号不能为空");
        }
        if (IdCard.CHINA_ID_MAX_LENGTH == idCard.length()) {
            return Short.valueOf(idCard.substring(6, 10));
        } else if (IdCard.CHINA_ID_MIN_LENGTH == idCard.length()) {
            return Short.valueOf("19" + idCard.substring(6, 8));
        } else {
            throw new BaseRuntimeException("10000001", "身份证号只能是18或者15位");
        }
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard
     *            身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        if (Strings.isBlank(idCard)) {
            throw new BaseRuntimeException("10000001", "身份证号不能为空");
        }
        if (IdCard.CHINA_ID_MAX_LENGTH == idCard.length()) {
            return Short.valueOf(idCard.substring(10, 12));
        } else if (IdCard.CHINA_ID_MIN_LENGTH == idCard.length()) {
            return Short.valueOf(idCard.substring(8, 10));
        } else {
            throw new BaseRuntimeException("10000001", "身份证号只能是18或者15位");
        }
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard
     *            身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
        if (Strings.isBlank(idCard)) {
            throw new BaseRuntimeException("10000001", "身份证号不能为空");
        }
        if (IdCard.CHINA_ID_MAX_LENGTH == idCard.length()) {
            return Short.valueOf(idCard.substring(12, 14));
        } else if (IdCard.CHINA_ID_MIN_LENGTH == idCard.length()) {
            return Short.valueOf(idCard.substring(10, 12));
        } else {
            throw new BaseRuntimeException("10000001", "身份证号只能是18或者15位");
        }
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard
     *            身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        if (Strings.isBlank(idCard)) {
            throw new BaseRuntimeException("10000001", "身份证号不能为空");
        }
        String sGender = "未知";
        String sCardNum = null;
        if (IdCard.CHINA_ID_MAX_LENGTH == idCard.length()) {
            sCardNum = idCard.substring(16, 17);
        } else if (IdCard.CHINA_ID_MIN_LENGTH == idCard.length()) {
            sCardNum = idCard.substring(14, 15);
        } else {
            throw new BaseRuntimeException("10000001", "身份证号只能是18或者15位");
        }

        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "M";// 男
        } else {
            sGender = "F";// 女
        }
        return sGender;
    }

    // 修改年龄的方法
    /*
     * @RequestMapping(value = "shebaoshuju",method = RequestMethod.GET) public String shebaoshuju(){ List<Wcbxx> list=
     * wcbxxservice.all(); int cou=0; for(Wcbxx w:list){ if(w.getIdcard()!=null && w.getIdcard().length()>17) { String
     * sex = IdCard.getGenderByIdCard(w.getIdcard()); int age = IdCard.getAgeByIdCard(w.getIdcard());
     * w.setAge(Long.valueOf(age)); w.setSex(sex); int a = wcbxxservice.updatess(w); if (a>0) { System.out.println("姓名"
     * + w.getRname() + "身份证" + w.getIdcard() + "年龄:" + age + "性别:" + sex); } } cou++; System.out.println(cou); } return
     * ""; }
     */

    public static void main(String[] a) {
        System.out.println("460200199209275127:");
        String idcard = "460200199209275127";
        String sex = getGenderByIdCard(idcard);
        System.out.println("性别:" + sex);
        int age = getAgeByIdCard(idcard);
        System.out.println("年龄:" + age);
        Short nian = getYearByIdCard(idcard);
        Short yue = getMonthByIdCard(idcard);
        Short ri = getDateByIdCard(idcard);
        System.out.println(nian + "年" + yue + "月" + ri + "日");

        String sr = getBirthByIdCard(idcard);
        System.out.println("生日:" + sr);
        
        Date birthday = getBirthdayByIdCard(idcard);
        System.out.println("格式化生日:" + Dates.formatDate(birthday, "yyyy-MM-dd"));

        System.out.println("420400700101001:");
        idcard = "420400700101001";
        sex = getGenderByIdCard(idcard);
        System.out.println("性别:" + sex);
        age = getAgeByIdCard(idcard);
        System.out.println("年龄:" + age);
        nian = getYearByIdCard(idcard);
        yue = getMonthByIdCard(idcard);
        ri = getDateByIdCard(idcard);
        System.out.println(nian + "年" + yue + "月" + ri + "日");

        sr = getBirthByIdCard(idcard);
        System.out.println("生日:" + sr);
    }

}
