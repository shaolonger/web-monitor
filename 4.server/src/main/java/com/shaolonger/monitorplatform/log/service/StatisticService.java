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

        // 创建一个待填充的数据表
        Map<String, Object> resultMap = new LinkedHashMap<>();
        long hoursGap = DateUtils.getHoursBetweenDateRange(startTime, endTime);
        for (long i = 0; i < hoursGap; i++) {
            Date startDate = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
            startDate.setTime(startDate.getTime() + (i + 1) * 3600 * 1000);
            resultMap.put(DateUtils.dateToStr(startDate, "yyyy-MM-dd HH"), 0);
        }

        List<Map<String, Object>> searchList = jsErrorLogService.getLogCountByHours(startTime, endTime);
        for (Map<String, Object> map : searchList) {
            resultMap.put((String) map.get("hour"), map.get("count"));
        }

        return resultMap;
    }
}
