package com.shaolonger.monitorplatform.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    /**
     * String转Date
     *
     * @param str     时间字符串
     * @param pattern 转换日期格式，如"yyyy-MM-dd HH:mm:ss"
     * @return Date
     */
    public static Date strToDate(String str, String pattern) {
        Date date = null;
        if (str != null && pattern != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                date = sdf.parse(str);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * Date转String
     *
     * @param date    日期
     * @param pattern 转换日期格式，如"yyyy-MM-dd HH:mm:ss"
     * @return String
     */
    public static String dateToStr(Date date, String pattern) {
        String dateStr = null;
        if (date != null && pattern != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 获取两个日期之间相差的小时数
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return long
     */
    public static long getHoursBetweenDateRange(Date startDate, Date endDate) {
        return Math.abs((endDate.getTime() - startDate.getTime()) / (3600 * 1000));
    }

    /**
     * 获取两个日期之间相差的天数
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return long
     */
    public static long getDaysBetweenDateRange(Date startDate, Date endDate) {
        return Math.abs((endDate.getTime() - startDate.getTime()) / (24 * 3600 * 1000));
    }

    /**
     * 根据传入的单位(秒)，获取两个日期之间相差的单位数
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param timeInterval 间隔时间
     * @return long
     */
    public static long getCountBetweenDateRange(Date startDate, Date endDate, int timeInterval) {
        return Math.abs((endDate.getTime() - startDate.getTime()) / (timeInterval * 1000));
    }

    /**
     * 获取在参考日期相隔days天的日期
     *
     * @param date 参考日期
     * @param days 相隔天数
     * @return Date
     */
    public static Date getDateBeforeOrAfterByDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}
