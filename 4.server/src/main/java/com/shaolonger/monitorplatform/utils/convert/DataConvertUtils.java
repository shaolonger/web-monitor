package com.shaolonger.monitorplatform.utils.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.rmi.CORBA.ValueHandler;
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
     * String转Integer或null
     * @param str 字符串
     * @return int
     */
    public static Integer strToIntegerOrNull(String str) {
        Integer i = null;
        try {
            i = Integer.valueOf(str);
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

    /**
     * String转String，返回字符串或空字符串
     * @param str 字符串
     * @return String
     */
    public static String getStrOrEmpty(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param jsonStr jsonStr
     * @param tClass tClass
     * @param <T> T
     * @return T
     * @throws JsonProcessingException
     */
    public static <T> T jsonStrToObject(String jsonStr, Class<T> tClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr, tClass);
    }
}
