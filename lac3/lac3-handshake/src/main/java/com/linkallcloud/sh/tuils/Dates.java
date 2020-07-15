package com.linkallcloud.sh.tuils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 */
public class Dates extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns =
            { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
                    "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
     * "yyyy/MM/dd HH:mm", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     * 
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     * 
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     * 
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * 
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     * 
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * yyyy-MM-dd
     * 
     * @param date
     * @return date
     */
    public static Date getSimpleDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.clear(Calendar.MILLISECOND);
            calendar.clear(Calendar.SECOND);
            calendar.clear(Calendar.MINUTE);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * 
     * @param dt
     * @param years
     * @return date
     */
    public static Date addYear(Date dt, int years) {
        GregorianCalendar thisday = new GregorianCalendar();
        thisday.setTime(dt);
        thisday.add(GregorianCalendar.YEAR, years);
        return thisday.getTime();
    }

    /**
     * 
     * @param dt
     * @param m
     * @return date
     */
    public static Date addMonth(Date dt, int m) {
        GregorianCalendar thisday = new GregorianCalendar();
        thisday.setTime(dt);
        thisday.add(GregorianCalendar.MONTH, m);
        return thisday.getTime();
    }

    /**
     * 
     * @param dt
     * @param d
     * @return date
     */
    public static Date addDate(Date dt, int d) {
        GregorianCalendar thisday = new GregorianCalendar();
        thisday.setTime(dt);
        thisday.add(GregorianCalendar.DATE, d);
        return thisday.getTime();
    }

    /**
     * 
     * @param dt
     * @param h
     * @return date
     */
    public static Date addHour(Date dt, int h) {
        GregorianCalendar thisday = new GregorianCalendar();
        thisday.setTime(dt);
        thisday.add(GregorianCalendar.HOUR_OF_DAY, h);
        return thisday.getTime();
    }

    /**
     * 
     * @param dt
     * @param m
     * @return date
     */
    public static Date addMinute(Date dt, int m) {
        GregorianCalendar thisday = new GregorianCalendar();
        thisday.setTime(dt);
        thisday.add(GregorianCalendar.MINUTE, m);
        return thisday.getTime();
    }

    /**
     * 
     * @param dt
     * @param s
     * @return date
     */
    public static Date addSecond(Date dt, int s) {
        GregorianCalendar thisday = new GregorianCalendar();
        thisday.setTime(dt);
        thisday.add(GregorianCalendar.SECOND, s);
        return thisday.getTime();
    }

    /**
     * 
     * @param year
     * @param month
     * @param day
     * @return
     * @throws BaseException
     */
    public static Date form(int year, int month, int day) throws Exception {
        String ds = String.valueOf(year) + "-" + month + "-" + day;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        return df.parse(ds);
    }

    /**
     * 得到当前时间的年月日
     * 
     * @return
     */
    public static int[] getNowCalendar() {
        Calendar cal = Calendar.getInstance();
        int[] result = { cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE) };
        return result;
    }

    /**
     * year、month、day所对应的日期和当前时间比较大小，小于当前返回-1，等于当前返回0，大于当前返回1。
     * 
     * @param year
     * @param month
     * @param day
     * @return
     * @throws BaseException
     */
    public static int compareNow(int year, int month, int day) throws Exception {
        Date date = Dates.form(year, month, day);
        int[] nowParam = Dates.getNowCalendar();
        Date now = Dates.form(nowParam[0], nowParam[1], nowParam[2]);
        return date.compareTo(now);
    }

    /**
     * year、month所对应的年月和当前时间比较大小，小于当前返回-1，等于当前返回0，大于当前返回1。
     * 
     * @param year
     * @param month
     * @return
     */
    public static int compareNow(int year, int month) {
        int[] nowParam = Dates.getNowCalendar();
        if (year < nowParam[0] || (year == nowParam[0] && month < nowParam[1])) {
            return -1;
        } else if (year == nowParam[0] && month == nowParam[1]) {
            return 0;
        } else {
            return 1;
        }
    }

    // 01. java.util.Date --> java.time.LocalDateTime
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    // 02. java.util.Date --> java.time.LocalDate
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    // 03. java.util.Date --> java.time.LocalTime
    public static LocalTime dateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    // 04. java.time.LocalDateTime --> java.util.Date
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    // 05. java.time.LocalDate --> java.util.Date
    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    // 06. java.time.LocalTime --> java.util.Date
    public static Date localTimeToDate(LocalTime localTime, LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

}
