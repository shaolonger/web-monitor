package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.utils.DateUtils;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class StatisticService {

    @Autowired
    CustomErrorLogService customErrorLogService;
    @Autowired
    HttpErrorLogService httpErrorLogService;
    @Autowired
    JsErrorLogService jsErrorLogService;
    @Autowired
    ResourceLoadErrorLogService resourceLoadErrorLogService;

    /**
     * 获取总览统计信息
     *
     * @param request request
     * @return Object
     */
    public Object getOverallByTimeRange(HttpServletRequest request) throws Exception {

        // 获取查询参数
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");

        // 校验参数
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");
        int customErrorLogCount = customErrorLogService.getCountByIdBetweenStartTimeAndEndTime(startTime, endTime);
        int httpErrorLogCount = httpErrorLogService.getCountByIdBetweenStartTimeAndEndTime(startTime, endTime);
        int jsErrorLogCount = jsErrorLogService.getCountByIdBetweenStartTimeAndEndTime(startTime, endTime);
        int resourceLoadErrorLogCount = resourceLoadErrorLogService.getCountByIdBetweenStartTimeAndEndTime(startTime, endTime);

        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("customErrorLogCount", customErrorLogCount);
        resultMap.put("httpErrorLogCount", httpErrorLogCount);
        resultMap.put("jsErrorLogCount", jsErrorLogCount);
        resultMap.put("resourceLoadErrorLogCount", resourceLoadErrorLogCount);

        return resultMap;
    }

    /**
     * 获取JsErrorLog日志统计信息
     *
     * @param request request
     * @return Object
     */
    public Object getJsErrorLogCountByHours(HttpServletRequest request) throws Exception {
        // 获取查询参数
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");

        // 校验参数
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");

        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        long hoursGap = DateUtils.getHoursBetweenDateRange(startTime, endTime);
        Map<String, Object> nowMap = new LinkedHashMap<>(); // 现在的数据
        Map<String, Object> agoMap = new LinkedHashMap<>(); // 七天前的数据

        for (long i = 0; i < hoursGap; i++) {
            Date nowtStartDate = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
            Date agoStartDate = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
            nowtStartDate.setTime(nowtStartDate.getTime() + (i + 1) * 3600 * 1000);
            agoStartDate.setTime(agoStartDate.getTime() + (i + 1) * 3600 * 1000 - 7 * 24 * 3600 * 1000);
            nowMap.put(DateUtils.dateToStr(nowtStartDate, "yyyy-MM-dd HH"), 0);
            agoMap.put(DateUtils.dateToStr(agoStartDate, "yyyy-MM-dd HH"), 0);
        }

        List<Map<String, Object>> nowSearchList = jsErrorLogService.getLogCountByHours(startTime, endTime);
        for (Map<String, Object> map : nowSearchList) {
            nowMap.put((String) map.get("hour"), map.get("count"));
        }

        Date agoStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -7);
        Date agoEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -7);
        List<Map<String, Object>> agoSearchList = jsErrorLogService.getLogCountByHours(agoStartTime, agoEndTime);
        for (Map<String, Object> map : agoSearchList) {
            agoMap.put((String) map.get("hour"), map.get("count"));
        }

        resultMap.put("now", nowMap);
        resultMap.put("ago", agoMap);

        return resultMap;
    }
}
