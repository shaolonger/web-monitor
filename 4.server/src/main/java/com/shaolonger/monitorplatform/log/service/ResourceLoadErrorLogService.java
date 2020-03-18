package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.log.dao.ResourceLoadErrorLogDao;
import com.shaolonger.monitorplatform.log.entity.ResourceLoadErrorLog;
import com.shaolonger.monitorplatform.utils.DateUtils;
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
import java.util.List;
import java.util.Map;

@Service
public class ResourceLoadErrorLogService extends ServiceBase {

    @Autowired
    private ResourceLoadErrorLogDao resourceLoadErrorLogDao;

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
        String resourceUrl = request.getParameter("resourceUrl");
        String resourceType = request.getParameter("resourceType");
        String status = request.getParameter("status");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from lms_resource_load_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from lms_resource_load_error_log t where 1=1");
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
        // 资源链接
        if (resourceUrl != null && !resourceUrl.isEmpty()) {
            paramSqlBuilder.append(" and t.resource_url like :resourceUrl");
            paramMap.put("resourceUrl", "%" + resourceUrl + "%");
        }
        // 资源类型
        if (resourceType != null && !resourceType.isEmpty()) {
            paramSqlBuilder.append(" and t.resource_type like :resourceType");
            paramMap.put("resourceType", "%" + resourceType + "%");
        }
        // 状态
        if (status != null && !status.isEmpty()) {
            paramSqlBuilder.append(" and t.status like :status");
            paramMap.put("status", "%" + status + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<ResourceLoadErrorLog> page = this.findPageBySqlAndParam(ResourceLoadErrorLog.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<ResourceLoadErrorLog> pageResultBase = new PageResultBase<>();
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
     * @param resourceLoadErrorLog resourceLoadErrorLog
     * @return Object
     */
    public Object add(ResourceLoadErrorLog resourceLoadErrorLog) {

        logger.info("--------[resourceLoadErrorLogService]保存开始--------");

        // 获取请求参数
        String logType = resourceLoadErrorLog.getLogType();
        Long userId = resourceLoadErrorLog.getUserId();
        String userName = resourceLoadErrorLog.getUserName();
        String pageUrl = resourceLoadErrorLog.getPageUrl();
        String pageKey = resourceLoadErrorLog.getPageKey();
        String deviceName = resourceLoadErrorLog.getDeviceName();
        String os = resourceLoadErrorLog.getOs();
        String browserName = resourceLoadErrorLog.getBrowserName();
        String browserVersion = resourceLoadErrorLog.getBrowserVersion();
        String ipAddress = resourceLoadErrorLog.getIpAddress();
        String resourceUrl = resourceLoadErrorLog.getResourceUrl();
        String resourceType = resourceLoadErrorLog.getResourceType();
        String status = resourceLoadErrorLog.getStatus();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        resourceLoadErrorLog.setLogType(logType);
        resourceLoadErrorLog.setUserId(userId);
        resourceLoadErrorLog.setUserName(userName);
        resourceLoadErrorLog.setPageUrl(pageUrl);
        resourceLoadErrorLog.setPageKey(pageKey);
        resourceLoadErrorLog.setDeviceName(deviceName);
        resourceLoadErrorLog.setOs(os);
        resourceLoadErrorLog.setBrowserName(browserName);
        resourceLoadErrorLog.setBrowserVersion(browserVersion);
        resourceLoadErrorLog.setIpAddress(ipAddress);
        resourceLoadErrorLog.setResourceUrl(resourceUrl);
        resourceLoadErrorLog.setResourceType(resourceType);
        resourceLoadErrorLog.setStatus(status);
        resourceLoadErrorLog.setCreateTime(createTime);
        resourceLoadErrorLogDao.save(resourceLoadErrorLog);

        logger.info("--------[resourceLoadErrorLogService]保存结束--------");

        return resourceLoadErrorLog;
    }

    /**
     * 查询某个时间段内的日志总数
     *
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return int
     */
    public int getCountByIdBetweenStartTimeAndEndTime(Date startTime, Date endTime) {
        return resourceLoadErrorLogDao.getCountByIdBetweenStartTimeAndEndTime(startTime, endTime);
    }

    /**
     * 获取日志统计信息
     *
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return List
     */
    public List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime, String projectIdentifier) {
        return resourceLoadErrorLogDao.getLogCountByHours(startTime, endTime, projectIdentifier);
    }
}
