package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.log.dao.CustomErrorLogDao;
import com.shaolonger.monitorplatform.log.entity.CustomErrorLog;
import com.shaolonger.monitorplatform.utils.PageResultBase;
import com.shaolonger.monitorplatform.utils.ServiceBase;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomErrorLogService extends ServiceBase {

    @Autowired
    private CustomErrorLogDao customErrorLogDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 条件查询
     *
     * @param request request
     * @return Object
     */
    public Object get(HttpServletRequest request) {
        // 获取请求参数
        int pageNum = DataConvertUtils.strToInt(request.getParameter("pageNum"));
        int pageSize = DataConvertUtils.strToInt(request.getParameter("pageSize"));
        String projectIdentifier = request.getParameter("projectIdentifier");
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
        StringBuilder dataSqlBuilder = new StringBuilder("select * from lms_custom_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from lms_custom_error_log t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 项目标识
        if (projectIdentifier != null && !projectIdentifier.isEmpty()) {
            paramSqlBuilder.append(" and t.project_identifier = :projectIdentifier");
            paramMap.put("projectIdentifier", projectIdentifier);
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
            paramSqlBuilder.append(" and t.user_name = :userName");
            paramMap.put("userName", "%" + userName + "%");
        }
        // 页面URL
        if (pageUrl != null && !pageUrl.isEmpty()) {
            paramSqlBuilder.append(" and t.page_url like :pageUrl");
            paramMap.put("pageUrl", "%" + pageUrl + "%");
        }
        // JS错误类型
        if (errorType != null && !errorType.isEmpty()) {
            paramSqlBuilder.append(" and t.error_type like :errorType");
            paramMap.put("errorType", "%" + errorType + "%");
        }
        // JS错误信息
        if (errorMessage != null && !errorMessage.isEmpty()) {
            paramSqlBuilder.append(" and t.error_message like :errorMessage");
            paramMap.put("errorMessage", "%" + errorMessage + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<CustomErrorLog> page = this.findPageBySqlAndParam(CustomErrorLog.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<CustomErrorLog> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(page.getContent());
        return pageResultBase;
    }

    /**
     * 新增
     *
     * @param customErrorLog customErrorLog
     * @return Object
     */
    public Object add(CustomErrorLog customErrorLog) {

        logger.info("--------[CustomErrorLogService]保存开始--------");

        // 获取请求参数
        String logType = customErrorLog.getLogType();
        Long userId = customErrorLog.getUserId();
        String userName = customErrorLog.getUserName();
        String pageUrl = customErrorLog.getPageUrl();
        String pageKey = customErrorLog.getPageKey();
        String deviceName = customErrorLog.getDeviceName();
        String os = customErrorLog.getOs();
        String browserName = customErrorLog.getBrowserName();
        String browserVersion = customErrorLog.getBrowserVersion();
        String ipAddress = customErrorLog.getIpAddress();
        String errorType = customErrorLog.getErrorType();
        String errorMessage = customErrorLog.getErrorMessage();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        customErrorLog.setLogType(logType);
        customErrorLog.setUserId(userId);
        customErrorLog.setUserName(userName);
        customErrorLog.setPageUrl(pageUrl);
        customErrorLog.setPageKey(pageKey);
        customErrorLog.setDeviceName(deviceName);
        customErrorLog.setOs(os);
        customErrorLog.setBrowserName(browserName);
        customErrorLog.setBrowserVersion(browserVersion);
        customErrorLog.setIpAddress(ipAddress);
        customErrorLog.setErrorType(errorType);
        customErrorLog.setErrorMessage(errorMessage);
        customErrorLog.setCreateTime(createTime);
        customErrorLogDao.save(customErrorLog);

        logger.info("--------[CustomErrorLogService]保存结束--------");

        return customErrorLog;
    }
}
