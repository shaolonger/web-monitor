package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.log.dao.CustomErrorLogDao;
import com.shaolonger.monitorplatform.log.dao.HttpErrorLogDao;
import com.shaolonger.monitorplatform.log.dao.JsErrorLogDao;
import com.shaolonger.monitorplatform.log.dao.ResourceLoadErrorLogDao;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@Service
public class StatisticService {

    @Autowired
    CustomErrorLogDao customErrorLogDao;

    @Autowired
    HttpErrorLogDao httpErrorLogDao;

    @Autowired
    JsErrorLogDao jsErrorLogDao;

    @Autowired
    ResourceLoadErrorLogDao resourceLoadErrorLogDao;

    /**
     * 获取总览统计信息
     *
     * @param request request
     * @return Object
     */
    public Object getOverall(HttpServletRequest request) throws Exception {

        // 获取查询参数
        Date startTime = DataConvertUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DataConvertUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");

        // 校验参数
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");
        int customErrorLogCount = customErrorLogDao.countByIdBetweenStartTimeAndEndTime(startTime, endTime);
        int httpErrorLogCount = httpErrorLogDao.countByIdBetweenStartTimeAndEndTime(startTime, endTime);
        int jsErrorLogCount = jsErrorLogDao.countByIdBetweenStartTimeAndEndTime(startTime, endTime);
        int resourceLoadErrorLogCount = resourceLoadErrorLogDao.countByIdBetweenStartTimeAndEndTime(startTime, endTime);

        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("customErrorLogCount", customErrorLogCount);
        resultMap.put("httpErrorLogCount", httpErrorLogCount);
        resultMap.put("jsErrorLogCount", jsErrorLogCount);
        resultMap.put("resourceLoadErrorLogCount", resourceLoadErrorLogCount);

        return resultMap;
    }
}
