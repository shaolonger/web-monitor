package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.log.dao.HttpErrorLogDao;
import com.shaolonger.monitorplatform.log.entity.HttpErrorLog;
import com.shaolonger.monitorplatform.utils.DateUtils;
import com.shaolonger.monitorplatform.common.api.PageResultBase;
import com.shaolonger.monitorplatform.common.service.ServiceBase;
import com.shaolonger.monitorplatform.utils.DataConvertUtils;
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
import java.util.List;
import java.util.Map;

@Service
public class HttpErrorLogService extends ServiceBase {

    @Autowired
    private HttpErrorLogDao httpErrorLogDao;

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
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        String userName = request.getParameter("userName");
        String pageUrl = request.getParameter("pageUrl");
        String httpType = request.getParameter("httpType");
        String httpUrlComplete = request.getParameter("httpUrlComplete");
        String httpUrlShort = request.getParameter("httpUrlShort");
        String status = request.getParameter("status");
        String statusText = request.getParameter("statusText");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from lms_http_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from lms_http_error_log t where 1=1");
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
            paramSqlBuilder.append(" and t.user_name like :userName");
            paramMap.put("userName", "%" + userName + "%");
        }
        // 页面URL
        if (pageUrl != null && !pageUrl.isEmpty()) {
            paramSqlBuilder.append(" and t.page_url like :pageUrl");
            paramMap.put("pageUrl", "%" + pageUrl + "%");
        }
        // Http类型，如request、response
        if (httpType != null && !httpType.isEmpty()) {
            paramSqlBuilder.append(" and t.http_type like :httpType");
            paramMap.put("httpType", "%" + httpType + "%");
        }
        // 完整的Http请求地址
        if (httpUrlComplete != null && !httpUrlComplete.isEmpty()) {
            paramSqlBuilder.append(" and t.http_url_complete like :httpUrlComplete");
            paramMap.put("httpUrlComplete", "%" + httpUrlComplete + "%");
        }
        // 缩写的Http请求地址
        if (httpUrlShort != null && !httpUrlShort.isEmpty()) {
            paramSqlBuilder.append(" and t.http_url_short like :httpUrlShort");
            paramMap.put("httpUrlShort", "%" + httpUrlShort + "%");
        }
        // 状态
        if (status != null && !status.isEmpty()) {
            paramSqlBuilder.append(" and t.status like :status");
            paramMap.put("status", "%" + status + "%");
        }
        // 状态的文字描述
        if (statusText != null && !statusText.isEmpty()) {
            paramSqlBuilder.append(" and t.status_text like :statusText");
            paramMap.put("statusText", "%" + statusText + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<HttpErrorLog> page = this.findPageBySqlAndParam(HttpErrorLog.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<HttpErrorLog> pageResultBase = new PageResultBase<>();
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
     * @param httpErrorLog httpErrorLog
     * @return Object
     */
    public Object add(HttpErrorLog httpErrorLog) {

        logger.info("--------[HttpErrorLogService]保存开始--------");

        // 获取请求参数
        String logType = httpErrorLog.getLogType();
        Long userId = httpErrorLog.getUserId();
        String userName = httpErrorLog.getUserName();
        String pageUrl = httpErrorLog.getPageUrl();
        String pageKey = httpErrorLog.getPageKey();
        String deviceName = httpErrorLog.getDeviceName();
        String os = httpErrorLog.getOs();
        String browserName = httpErrorLog.getBrowserName();
        String browserVersion = httpErrorLog.getBrowserVersion();
        String ipAddress = httpErrorLog.getIpAddress();
        String httpType = httpErrorLog.getHttpType();
        String httpUrlComplete = httpErrorLog.getHttpUrlComplete();
        String httpUrlShort = httpErrorLog.getHttpUrlShort();
        String status = httpErrorLog.getStatus();
        String statusText = httpErrorLog.getStatusText();
        String resTime = httpErrorLog.getResTime();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        httpErrorLog.setLogType(logType);
        httpErrorLog.setUserId(userId);
        httpErrorLog.setUserName(userName);
        httpErrorLog.setPageUrl(pageUrl);
        httpErrorLog.setPageKey(pageKey);
        httpErrorLog.setDeviceName(deviceName);
        httpErrorLog.setOs(os);
        httpErrorLog.setBrowserName(browserName);
        httpErrorLog.setBrowserVersion(browserVersion);
        httpErrorLog.setIpAddress(ipAddress);
        httpErrorLog.setHttpType(httpType);
        httpErrorLog.setHttpUrlComplete(httpUrlComplete);
        httpErrorLog.setHttpUrlShort(httpUrlShort);
        httpErrorLog.setStatus(status);
        httpErrorLog.setStatusText(statusText);
        httpErrorLog.setResTime(resTime);
        httpErrorLog.setCreateTime(createTime);
        httpErrorLogDao.save(httpErrorLog);

        logger.info("--------[HttpErrorLogService]保存结束--------");

        return httpErrorLog;
    }

    /**
     * 查询某个时间段内的日志总数
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return int
     */
    public int getCountByIdBetweenStartTimeAndEndTime(Date startTime, Date endTime) {
        return httpErrorLogDao.getCountByIdBetweenStartTimeAndEndTime(startTime, endTime);
    }

    /**
     * 按小时间隔，获取各小时内的日志数量
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime, String projectIdentifier) {
        return httpErrorLogDao.getLogCountByHours(startTime, endTime, projectIdentifier);
    }

    /**
     * 按天时间隔，获取各天内的日志数量
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogCountByDays(Date startTime, Date endTime, String projectIdentifier) {
        return httpErrorLogDao.getLogCountByDays(startTime, endTime, projectIdentifier);
    }

    /**
     * 按status分类获取日志数量
     *
     * @param request request
     * @return List
     */
    public List<Map<String, Object>> getLogCountByState(HttpServletRequest request) throws Exception {

        // 获取查询参数
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        String projectIdentifier = request.getParameter("projectIdentifier");

        // 参数校验
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");
        if (projectIdentifier == null || projectIdentifier.isEmpty()) throw new Exception("projectIdentifier错误");

        return httpErrorLogDao.getLogCountByState(startTime, endTime, projectIdentifier);
    }
}
