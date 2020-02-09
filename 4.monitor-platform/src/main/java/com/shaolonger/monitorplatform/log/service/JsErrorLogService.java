package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.log.dao.JsErrorLogDao;
import com.shaolonger.monitorplatform.log.entity.JsErrorLog;
import com.shaolonger.monitorplatform.utils.PageResultBase;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class JsErrorLogService {
    @Autowired
    private JsErrorLogDao jsErrorLogDao;

    /**
     * 条件查询
     * @param request
     * @return
     */
    public Object findByQueries(HttpServletRequest request) {
        // 获取请求参数
        int pageNum = DataConvertUtils.strToInt(request.getParameter("pageNum"));
        int pageSize = DataConvertUtils.strToInt(request.getParameter("pageSize"));
        String logType = request.getParameter("logType");
        Date startTime = DataConvertUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DataConvertUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        String userName = "%" + request.getParameter("userName") + "%";
        String pageUrl = "%" + request.getParameter("pageUrl") + "%";
        String errorType = "%" + request.getParameter("errorType") + "%";
        String errorMessage = "%" + request.getParameter("errorMessage") + "%";

        // 分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<JsErrorLog> page = jsErrorLogDao.findByQueries(logType, startTime, endTime, userName, pageUrl, errorType, errorMessage, pageable);
        List<JsErrorLog> resultList = page.getContent();

        // 返回
        PageResultBase<JsErrorLog> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(page.getContent());
        return pageResultBase;
    }

    /**
     * 新增
     * @param jsErrorLog
     * @return
     */
    public Object add(JsErrorLog jsErrorLog) {
        // 获取请求参数
        String logType = jsErrorLog.getLogType();
        Long userId = jsErrorLog.getUserId();
        String userName = jsErrorLog.getUserName();
        String pageUrl = jsErrorLog.getPageUrl();
        String pageKey = jsErrorLog.getPageKey();
        String deviceName = jsErrorLog.getDeviceName();
        String os = jsErrorLog.getOs();
        String browserName = jsErrorLog.getBrowserName();
        String browserVersion = jsErrorLog.getBrowserVersion();
        String ipAddress = jsErrorLog.getIpAddress();
        String errorType = jsErrorLog.getErrorType();
        String errorMessage = jsErrorLog.getErrorMessage();
        String errorStack = jsErrorLog.getErrorStack();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        jsErrorLog.setLogType(logType);
        jsErrorLog.setUserId(userId);
        jsErrorLog.setUserName(userName);
        jsErrorLog.setPageUrl(pageUrl);
        jsErrorLog.setPageKey(pageKey);
        jsErrorLog.setDeviceName(deviceName);
        jsErrorLog.setOs(os);
        jsErrorLog.setBrowserName(browserName);
        jsErrorLog.setBrowserVersion(browserVersion);
        jsErrorLog.setIpAddress(ipAddress);
        jsErrorLog.setErrorType(errorType);
        jsErrorLog.setErrorMessage(errorMessage);
        jsErrorLog.setErrorStack(errorStack);
        jsErrorLog.setCreateTime(createTime);
        jsErrorLogDao.save(jsErrorLog);

        return jsErrorLog;
    }
}
