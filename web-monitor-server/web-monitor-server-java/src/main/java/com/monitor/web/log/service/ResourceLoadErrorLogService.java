package com.monitor.web.log.service;

import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.log.entity.ResourceLoadErrorLogEntity;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.utils.DateUtils;
import com.monitor.web.log.dao.ResourceLoadErrorLogDao;
import com.monitor.web.log.vo.StatisticRecordVO;
import com.monitor.web.common.service.ServiceBase;
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

    @Autowired
    private StatisticService statisticService;

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
        String bUname = request.getParameter("bUname");
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
        if (bUname != null && !bUname.isEmpty()) {
            paramSqlBuilder.append(" and t.b_uname like :bUname");
            paramMap.put("bUname", "%" + bUname + "%");
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
        Page<ResourceLoadErrorLogEntity> page = this.findPageBySqlAndParam(ResourceLoadErrorLogEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<ResourceLoadErrorLogEntity> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(page.getContent());
        return pageResultBase;
    }


    /**
     * 条件查询
     *
     * @param request request
     * @return Object
     */
    public Object getByGroup(HttpServletRequest request) {
        // 获取请求参数
        int pageNum = DataConvertUtils.strToInt(request.getParameter("pageNum"));
        int pageSize = DataConvertUtils.strToInt(request.getParameter("pageSize"));
        String projectIdentifier = request.getParameter("projectIdentifier");
        String logType = request.getParameter("logType");
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        String bUname = request.getParameter("bUname");
        String pageUrl = request.getParameter("pageUrl");
        String resourceUrl = request.getParameter("resourceUrl");
        String resourceType = request.getParameter("resourceType");
        String status = request.getParameter("status");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select count(t.id) as count, max(t.create_time) as latest_create_time, " +
                "count(distinct t.c_uuid) as user_count, t.resource_url from lms_resource_load_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(*) from (select count(t.id) from lms_resource_load_error_log t where 1=1");
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
        if (bUname != null && !bUname.isEmpty()) {
            paramSqlBuilder.append(" and t.b_uname like :bUname");
            paramMap.put("bUname", "%" + bUname + "%");
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
        dataSqlBuilder.append(paramSqlBuilder).append(" group by t.resource_url order by count desc");
        countSqlBuilder.append(paramSqlBuilder).append(" group by t.resource_url) s");
        Page page = this.findPageByNativeSqlAndParam(dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 转换vo
        List<StatisticRecordVO> resultList = statisticService.getStatisticListVO(page.getContent());

        // 返回
        PageResultBase<StatisticRecordVO> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(resultList);
        return pageResultBase;
    }

    /**
     * 新增
     *
     * @param resourceLoadErrorLogEntity resourceLoadErrorLog
     * @return Object
     */
    public boolean add(ResourceLoadErrorLogEntity resourceLoadErrorLogEntity) {

        logger.info("--------[resourceLoadErrorLogService]保存开始--------");

        // 获取请求参数
        String logType = resourceLoadErrorLogEntity.getLogType();
        String cUuid = resourceLoadErrorLogEntity.getCUuid();
        Long bUid = resourceLoadErrorLogEntity.getBUid();
        String bUname = resourceLoadErrorLogEntity.getBUname();
        String pageUrl = resourceLoadErrorLogEntity.getPageUrl();
        String pageKey = resourceLoadErrorLogEntity.getPageKey();
        String deviceName = resourceLoadErrorLogEntity.getDeviceName();
        String os = resourceLoadErrorLogEntity.getOs();
        String osVersion = resourceLoadErrorLogEntity.getOsVersion();
        String browserName = resourceLoadErrorLogEntity.getBrowserName();
        String browserVersion = resourceLoadErrorLogEntity.getBrowserVersion();
        String ipAddress = resourceLoadErrorLogEntity.getIpAddress();
        String netType = resourceLoadErrorLogEntity.getNetType();
        String resourceUrl = resourceLoadErrorLogEntity.getResourceUrl();
        String resourceType = resourceLoadErrorLogEntity.getResourceType();
        String status = resourceLoadErrorLogEntity.getStatus();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        resourceLoadErrorLogEntity.setLogType(logType);
        resourceLoadErrorLogEntity.setCUuid(cUuid);
        resourceLoadErrorLogEntity.setBUid(bUid);
        resourceLoadErrorLogEntity.setBUname(bUname);
        resourceLoadErrorLogEntity.setPageUrl(pageUrl);
        resourceLoadErrorLogEntity.setPageKey(pageKey);
        resourceLoadErrorLogEntity.setDeviceName(deviceName);
        resourceLoadErrorLogEntity.setOs(os);
        resourceLoadErrorLogEntity.setOsVersion(osVersion);
        resourceLoadErrorLogEntity.setBrowserName(browserName);
        resourceLoadErrorLogEntity.setBrowserVersion(browserVersion);
        resourceLoadErrorLogEntity.setIpAddress(ipAddress);
        resourceLoadErrorLogEntity.setNetType(netType);
        resourceLoadErrorLogEntity.setResourceUrl(resourceUrl);
        resourceLoadErrorLogEntity.setResourceType(resourceType);
        resourceLoadErrorLogEntity.setStatus(status);
        resourceLoadErrorLogEntity.setCreateTime(createTime);
        resourceLoadErrorLogDao.save(resourceLoadErrorLogEntity);

        logger.info("--------[resourceLoadErrorLogService]保存结束--------");

        return true;
    }

    /**
     * 新增-LogService通用
     *
     * @param request request
     * @return Object
     */
    public boolean add(HttpServletRequest request) throws Exception {

        logger.info("--------[resourceLoadErrorLogService]保存开始--------");

        // 获取请求参数
        String projectIdentifier = request.getParameter("projectIdentifier");
        String logType = request.getParameter("logType");
        String cUuid = request.getParameter("cUuid");
        Long bUid = DataConvertUtils.strToLong(request.getParameter("bUid"));
        String bUname = request.getParameter("bUname");
        String pageUrl = request.getParameter("pageUrl");
        String pageKey = request.getParameter("pageKey");
        String deviceName = request.getParameter("deviceName");
        String os = request.getParameter("os");
        String osVersion = request.getParameter("osVersion");
        String browserName = request.getParameter("browserName");
        String browserVersion = request.getParameter("browserVersion");
        String ipAddress = request.getParameter("ipAddress");
        String netType = request.getParameter("netType");
        String resourceUrl = request.getParameter("resourceUrl");
        String resourceType = request.getParameter("resourceType");
        String status = request.getParameter("status");

        // 创建时间
        Date createTime = new Date();

        // 校验参数
        if (projectIdentifier == null || projectIdentifier.isEmpty()) {
            throw new Exception("projectIdentifier不能为空");
        }
        if (cUuid == null || cUuid.isEmpty()) {
            throw new Exception("cUuid不能为空");
        }
        if (logType == null || logType.isEmpty()) {
            throw new Exception("logType不能为空");
        }
        if (pageUrl == null || pageUrl.isEmpty()) {
            throw new Exception("pageUrl不能为空");
        }
        if (resourceUrl == null || resourceUrl.isEmpty()) {
            throw new Exception("resourceUrl不能为空");
        }
        if (resourceType == null || resourceType.isEmpty()) {
            throw new Exception("resourceType不能为空");
        }
        if (status == null || status.isEmpty()) {
            throw new Exception("status不能为空");
        }

        // 保存实体
        ResourceLoadErrorLogEntity resourceLoadErrorLogEntity = new ResourceLoadErrorLogEntity();
        resourceLoadErrorLogEntity.setProjectIdentifier(projectIdentifier);
        resourceLoadErrorLogEntity.setLogType(logType);
        resourceLoadErrorLogEntity.setCUuid(cUuid);
        resourceLoadErrorLogEntity.setBUid(bUid);
        resourceLoadErrorLogEntity.setBUname(bUname);
        resourceLoadErrorLogEntity.setPageUrl(pageUrl);
        resourceLoadErrorLogEntity.setPageKey(pageKey);
        resourceLoadErrorLogEntity.setDeviceName(deviceName);
        resourceLoadErrorLogEntity.setOs(os);
        resourceLoadErrorLogEntity.setOsVersion(osVersion);
        resourceLoadErrorLogEntity.setBrowserName(browserName);
        resourceLoadErrorLogEntity.setBrowserVersion(browserVersion);
        resourceLoadErrorLogEntity.setIpAddress(ipAddress);
        resourceLoadErrorLogEntity.setNetType(netType);
        resourceLoadErrorLogEntity.setResourceUrl(resourceUrl);
        resourceLoadErrorLogEntity.setResourceType(resourceType);
        resourceLoadErrorLogEntity.setStatus(status);
        resourceLoadErrorLogEntity.setCreateTime(createTime);
        resourceLoadErrorLogDao.save(resourceLoadErrorLogEntity);

        logger.info("--------[resourceLoadErrorLogService]保存结束--------");

        return true;
    }

    /**
     * 查询某个时间段内的日志总数
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return int
     */
    public int getCountByIdBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime) {
        return resourceLoadErrorLogDao.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);
    }

    /**
     * 按小时间隔，获取各小时内的日志数量
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return List
     */
    public List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime, String projectIdentifier) {
        return resourceLoadErrorLogDao.getLogCountByHours(startTime, endTime, projectIdentifier);
    }

    /**
     * 按天间隔，获取各天内的日志数量
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return List
     */
    public List<Map<String, Object>> getLogCountByDays(Date startTime, Date endTime, String projectIdentifier) {
        return resourceLoadErrorLogDao.getLogCountByDays(startTime, endTime, projectIdentifier);
    }

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
        String projectIdentifier = request.getParameter("projectIdentifier");

        // 参数校验
        if (startTime == null || endTime == null) throw new Exception("startTime或endTime不能为空");
        if (projectIdentifier == null || projectIdentifier.isEmpty()) throw new Exception("projectIdentifier错误");

        return resourceLoadErrorLogDao.getOverallByTimeRange(startTime, endTime, projectIdentifier);
    }

    /**
     * 获取时间间隔内的简易日志信息
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogListByCreateTimeAndProjectIdentifier(String projectIdentifier, Date startTime, Date endTime) {
        return resourceLoadErrorLogDao.getLogListByCreateTimeAndProjectIdentifier(projectIdentifier, startTime, endTime);
    }

    /**
     * 获取时间间隔内的所有日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getAllLogsBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime) {
        return resourceLoadErrorLogDao.findAllByProjectIdentifierAndCreateTimeBetween(projectIdentifier, startTime, endTime);
    }
}
