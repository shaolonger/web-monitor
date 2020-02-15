package com.shaolonger.monitorplatform.utils.convert;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataConvertUtils {
    /**
     * String转Date
     * @param str 时间字符串
     * @param pattern 转换日期格式，如"yyyy-MM-dd HH:mm:ss"
     * @return Date
     */
    public static Date strToDate(String str, String pattern) {
        Date date = null;
        if (str != null && pattern != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                date = sdf.parse(str);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * String转int
     * @param str 字符串
     * @return int
     */
    public static int strToInt(String str) {
        int i = 0;
        try {
            i = Integer.parseInt(str);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return i;
    }

    /**
     * String转Long
     * @param str 字符串
     * @return int
     */
    public static Long strToLong(String str) {
        Long l = null;
        try {
            l = Long.parseLong(str);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return l;
    }
}
