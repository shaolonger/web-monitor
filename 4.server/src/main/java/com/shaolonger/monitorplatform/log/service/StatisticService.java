package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class StatisticService {

    @Autowired
    JsErrorLogService jsErrorLogService;
    @Autowired
    HttpErrorLogService httpErrorLogService;
    @Autowired
    ResourceLoadErrorLogService resourceLoadErrorLogService;
    @Autowired
    CustomErrorLogService customErrorLogService;

    /**
     * 获取总览统计信息
     *
     * @param request request
     * @return Object
     */
    public Object getOverallByTimeRange(HttpServletRequest request) throws Exception {

        // 获取查询参数
        String projectIdentifier = request.getParameter("projectIdentifier");
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");

        // 校验参数
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");
        if (projectIdentifier == null || projectIdentifier.isEmpty()) throw new Exception("projectIdentifier错误");
        int customErrorLogCount = customErrorLogService.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);
        int httpErrorLogCount = httpErrorLogService.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);
        int jsErrorLogCount = jsErrorLogService.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);
        int resourceLoadErrorLogCount = resourceLoadErrorLogService.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);

        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("customErrorLogCount", customErrorLogCount);
        resultMap.put("httpErrorLogCount", httpErrorLogCount);
        resultMap.put("jsErrorLogCount", jsErrorLogCount);
        resultMap.put("resourceLoadErrorLogCount", resourceLoadErrorLogCount);

        return resultMap;
    }

    /**
     * 按小时间隔，获取各小时内的日志数量
     *
     * @param request request
     * @return Object
     */
    public Object getLogCountByHours(HttpServletRequest request) throws Exception {
        // 获取查询参数
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        String logType = request.getParameter("logType");
        String projectIdentifier = request.getParameter("projectIdentifier");

        // 校验参数
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");
        if (logType == null || logType.isEmpty()) throw new Exception("logType错误");
        if (projectIdentifier == null || projectIdentifier.isEmpty()) throw new Exception("projectIdentifier错误");

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

        List<Map<String, Object>> nowSearchList = null;
        List<Map<String, Object>> agoSearchList = null;
        Date agoStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -7);
        Date agoEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -7);

        switch (logType) {
            case "jsErrorLog":
                nowSearchList = jsErrorLogService.getLogCountByHours(startTime, endTime, projectIdentifier);
                agoSearchList = jsErrorLogService.getLogCountByHours(agoStartTime, agoEndTime, projectIdentifier);
                break;
            case "httpErrorLog":
                nowSearchList = httpErrorLogService.getLogCountByHours(startTime, endTime, projectIdentifier);
                agoSearchList = httpErrorLogService.getLogCountByHours(agoStartTime, agoEndTime, projectIdentifier);
                break;
            case "resourceLoadErrorLog":
                nowSearchList = resourceLoadErrorLogService.getLogCountByHours(startTime, endTime, projectIdentifier);
                agoSearchList = resourceLoadErrorLogService.getLogCountByHours(agoStartTime, agoEndTime, projectIdentifier);
                break;
            case "customErrorLog":
                nowSearchList = customErrorLogService.getLogCountByHours(startTime, endTime, projectIdentifier);
                agoSearchList = customErrorLogService.getLogCountByHours(agoStartTime, agoEndTime, projectIdentifier);
                break;
            default:
                break;
        }
        if (nowSearchList != null && agoSearchList != null) {
            for (Map<String, Object> map : nowSearchList) {
                nowMap.put((String) map.get("hour"), map.get("count"));
            }
            for (Map<String, Object> map : agoSearchList) {
                agoMap.put((String) map.get("hour"), map.get("count"));
            }
        }

        resultMap.put("now", nowMap);
        resultMap.put("ago", agoMap);

        return resultMap;
    }

    /**
     * 按天间隔，获取各天内的日志数量
     *
     * @param request request
     * @return Object
     */
    public Object getLogCountByDays(HttpServletRequest request) throws Exception {

        // 获取查询参数
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd");
        String logType = request.getParameter("logType");
        String projectIdentifier = request.getParameter("projectIdentifier");

        // 校验参数
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");
        if (logType == null || logType.isEmpty()) throw new Exception("logType错误");
        if (projectIdentifier == null || projectIdentifier.isEmpty()) throw new Exception("projectIdentifier错误");

        long daysGap = DateUtils.getDaysBetweenDateRange(startTime, endTime);
        Map<String, Object> resultMap = new LinkedHashMap<>();

        for (long i = 0; i < daysGap + 1; i++) {
            Date nowtStartDate = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd");
            nowtStartDate.setTime(nowtStartDate.getTime() + i * 24 * 3600 * 1000);
            resultMap.put(DateUtils.dateToStr(nowtStartDate, "yyyy-MM-dd"), 0);
        }

        List<Map<String, Object>> searchList = null;

        switch (logType) {
            case "jsErrorLog":
                searchList = jsErrorLogService.getLogCountByDays(startTime, endTime, projectIdentifier);
                break;
            case "httpErrorLog":
                searchList = httpErrorLogService.getLogCountByDays(startTime, endTime, projectIdentifier);
                break;
            case "resourceLoadErrorLog":
                searchList = resourceLoadErrorLogService.getLogCountByDays(startTime, endTime, projectIdentifier);
                break;
            case "customErrorLog":
                searchList = customErrorLogService.getLogCountByDays(startTime, endTime, projectIdentifier);
                break;
            default:
                break;
        }
        if (searchList != null) {
            for (Map<String, Object> map : searchList) {
                resultMap.put((String) map.get("day"), map.get("count"));
            }
        }

        return resultMap;
    }
}
