package com.shaolonger.monitorplatform.utils;

import java.util.Date;

public class DateUtils {

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
}
