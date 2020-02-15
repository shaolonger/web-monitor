package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.log.dao.JsErrorLogDao;
import com.shaolonger.monitorplatform.log.entity.JsErrorLog;
import com.shaolonger.monitorplatform.utils.PageResultBase;
import com.shaolonger.monitorplatform.utils.ServiceBase;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JsErrorLogService extends ServiceBase {

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
        Long projectId = DataConvertUtils.strToLong(request.getParameter("projectId"));
        String logType = request.getParameter("logType");
        Date startTime = DataConvertUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DataConvertUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        String userName = request.getParameter("userName");
        String pageUrl = request.getParameter("pageUrl");
        String errorType = request.getParameter("errorType");
        String errorMessage = request.getParameter("errorMessage");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from lms_js_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from lms_js_error_log t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 项目id
        if (projectId != null) {
            paramSqlBuilder.append(" and t.projectId = :projectId");
            paramMap.put("projectId", projectId);
        }
        // 日志类型
        if (logType != null && !logType.isEmpty()) {
            paramSqlBuilder.append(" and t.log_type like :logType");
            paramMap.put("logType", "%" + logType + "%");
        }
        // 开始时间、结束时间
        if (startTime != null && endTime != null) {
            paramSqlBuilder.append(" and t.create_time between :startTime and :endTime");
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", endTime);
        } else if (startTime != null) {
            paramSqlBuilder.append(" and t.create_time >= :startTime");
            paramMap.put("startTime", startTime);
        } else if (endTime != null) {
            paramSqlBuilder.append(" and t.create_time <= :endTime");
            paramMap.put("endTime", endTime);
        }
        // 用户名
        if (userName != null && !userName.isEmpty()) {
            paramSqlBuilder.append(" and t.userName = :userName");
            paramMap.put("userName", "%" + userName + "%");
        }
        // 页面URL
        if (pageUrl != null && !pageUrl.isEmpty()) {
            paramSqlBuilder.append(" and t.pageUrl like :pageUrl");
            paramMap.put("pageUrl", "%" + pageUrl + "%");
        }
        // JS错误类型
        if (errorType != null && !errorType.isEmpty()) {
            paramSqlBuilder.append(" and t.errorType like :errorType");
            paramMap.put("errorType", "%" + errorType + "%");
        }
        // JS错误信息
        if (errorMessage != null && !errorMessage.isEmpty()) {
            paramSqlBuilder.append(" and t.errorMessage like :errorMessage");
            paramMap.put("errorMessage", "%" + errorMessage + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<JsErrorLog> page = this.findPageBySqlAndParam(JsErrorLog.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);
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
