package com.monitor.web.log.service;

import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.log.dao.CustomErrorLogDAO;
import com.monitor.web.log.entity.CustomErrorLogEntity;
import com.monitor.web.log.vo.StatisticRecordVO;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.utils.DateUtils;
import com.monitor.web.common.service.ServiceBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CustomErrorLogService extends ServiceBase {

    @Autowired
    private CustomErrorLogDAO customErrorLogDao;

    @Autowired
    private StatisticService statisticService;

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
        String errorType = request.getParameter("errorType");
        String errorMessage = request.getParameter("errorMessage");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from lms_custom_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from lms_custom_error_log t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 项目标识
        if (!StringUtils.isEmpty(projectIdentifier)) {
            paramSqlBuilder.append(" and t.project_identifier = :projectIdentifier");
            paramMap.put("projectIdentifier", projectIdentifier);
        }
        // 日志类型
        if (!StringUtils.isEmpty(logType)) {
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
        if (!StringUtils.isEmpty(bUname)) {
            paramSqlBuilder.append(" and t.b_uname like :bUname");
            paramMap.put("bUname", "%" + bUname + "%");
        }
        // 页面URL
        if (!StringUtils.isEmpty(pageUrl)) {
            paramSqlBuilder.append(" and t.page_url like :pageUrl");
            paramMap.put("pageUrl", "%" + pageUrl + "%");
        }
        // JS错误类型
        if (!StringUtils.isEmpty(errorType)) {
            paramSqlBuilder.append(" and t.error_type like :errorType");
            paramMap.put("errorType", "%" + errorType + "%");
        }
        // JS错误信息
        if (!StringUtils.isEmpty(errorMessage)) {
            paramSqlBuilder.append(" and t.error_message like :errorMessage");
            paramMap.put("errorMessage", "%" + errorMessage + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<CustomErrorLogEntity> page = this.findPageBySqlAndParam(CustomErrorLogEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<CustomErrorLogEntity> pageResultBase = new PageResultBase<>();
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
        String errorType = request.getParameter("errorType");
        String errorMessage = request.getParameter("errorMessage");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select count(t.id) as count, max(t.create_time) as latest_create_time, " +
                "count(distinct t.c_uuid) as user_count, t.error_message from lms_custom_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(*) from (select count(t.id) as count, max(t.create_time) " +
                "as latest_create_time, count(t.c_uuid) as user_count, t.error_message from lms_custom_error_log t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 项目标识
        if (!StringUtils.isEmpty(projectIdentifier)) {
            paramSqlBuilder.append(" and t.project_identifier = :projectIdentifier");
            paramMap.put("projectIdentifier", projectIdentifier);
        }
        // 日志类型
        if (!StringUtils.isEmpty(logType)) {
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
        if (!StringUtils.isEmpty(bUname)) {
            paramSqlBuilder.append(" and t.b_uname like :bUname");
            paramMap.put("bUname", "%" + bUname + "%");
        }
        // 页面URL
        if (!StringUtils.isEmpty(pageUrl)) {
            paramSqlBuilder.append(" and t.page_url like :pageUrl");
            paramMap.put("pageUrl", "%" + pageUrl + "%");
        }
        // JS错误类型
        if (!StringUtils.isEmpty(errorType)) {
            paramSqlBuilder.append(" and t.error_type like :errorType");
            paramMap.put("errorType", "%" + errorType + "%");
        }
        // JS错误信息
        if (!StringUtils.isEmpty(errorMessage)) {
            paramSqlBuilder.append(" and t.error_message like :errorMessage");
            paramMap.put("errorMessage", "%" + errorMessage + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" group by t.error_message order by count desc");
        countSqlBuilder.append(paramSqlBuilder.append(" group by t.error_message) s"));
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
     * @param customErrorLogEntity customErrorLog
     * @return Object
     */
    public boolean add(CustomErrorLogEntity customErrorLogEntity) {

        log.info("--------[CustomErrorLogService]保存开始--------");

        // 获取请求参数
        String logType = customErrorLogEntity.getLogType();
        String cUuid = customErrorLogEntity.getCUuid();
        Long userId = customErrorLogEntity.getBUid();
        String bUname = customErrorLogEntity.getBUname();
        String pageUrl = customErrorLogEntity.getPageUrl();
        String pageKey = customErrorLogEntity.getPageKey();
        String deviceName = customErrorLogEntity.getDeviceName();
        String os = customErrorLogEntity.getOs();
        String osVersion = customErrorLogEntity.getOsVersion();
        String browserName = customErrorLogEntity.getBrowserName();
        String browserVersion = customErrorLogEntity.getBrowserVersion();
        String ipAddress = customErrorLogEntity.getIpAddress();
        String netType = customErrorLogEntity.getNetType();
        String errorType = customErrorLogEntity.getErrorType();
        String errorMessage = customErrorLogEntity.getErrorMessage();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        customErrorLogEntity.setLogType(logType);
        customErrorLogEntity.setCUuid(cUuid);
        customErrorLogEntity.setBUid(userId);
        customErrorLogEntity.setBUname(bUname);
        customErrorLogEntity.setPageUrl(pageUrl);
        customErrorLogEntity.setPageKey(pageKey);
        customErrorLogEntity.setDeviceName(deviceName);
        customErrorLogEntity.setOs(os);
        customErrorLogEntity.setOsVersion(osVersion);
        customErrorLogEntity.setBrowserName(browserName);
        customErrorLogEntity.setBrowserVersion(browserVersion);
        customErrorLogEntity.setIpAddress(ipAddress);
        customErrorLogEntity.setNetType(netType);
        customErrorLogEntity.setErrorType(errorType);
        customErrorLogEntity.setErrorMessage(errorMessage);
        customErrorLogEntity.setCreateTime(createTime);
        customErrorLogDao.save(customErrorLogEntity);

        log.info("--------[CustomErrorLogService]保存结束--------");

        return true;
    }

    /**
     * 新增-LogService通用
     *
     * @param request request
     * @return Object
     */
    public boolean add(HttpServletRequest request) throws Exception {

        log.info("--------[CustomErrorLogService]保存开始--------");

        // 获取请求参数
        String projectIdentifier = request.getParameter("projectIdentifier");
        String logType = request.getParameter("logType");
        String cUuid = request.getParameter("cUuid");
        Long userId = DataConvertUtils.strToLong(request.getParameter("userId"));
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
        String errorType = request.getParameter("errorType");
        String errorMessage = request.getParameter("errorMessage");

        // 创建时间
        Date createTime = new Date();

        // 校验参数
        if (!StringUtils.isEmpty(projectIdentifier)) {
            throw new Exception("projectIdentifier不能为空");
        }
        if (!StringUtils.isEmpty(cUuid)) {
            throw new Exception("cUuid不能为空");
        }
        if (!StringUtils.isEmpty(logType)) {
            throw new Exception("logType不能为空");
        }
        if (!StringUtils.isEmpty(pageUrl)) {
            throw new Exception("pageUrl不能为空");
        }
        if (!StringUtils.isEmpty(errorMessage)) {
            throw new Exception("errorMessage不能为空");
        }

        // 保存实体
        CustomErrorLogEntity customErrorLogEntity = new CustomErrorLogEntity();
        customErrorLogEntity.setProjectIdentifier(projectIdentifier);
        customErrorLogEntity.setLogType(logType);
        customErrorLogEntity.setCUuid(cUuid);
        customErrorLogEntity.setBUid(userId);
        customErrorLogEntity.setBUname(bUname);
        customErrorLogEntity.setPageUrl(pageUrl);
        customErrorLogEntity.setPageKey(pageKey);
        customErrorLogEntity.setDeviceName(deviceName);
        customErrorLogEntity.setOs(os);
        customErrorLogEntity.setOsVersion(osVersion);
        customErrorLogEntity.setBrowserName(browserName);
        customErrorLogEntity.setBrowserVersion(browserVersion);
        customErrorLogEntity.setIpAddress(ipAddress);
        customErrorLogEntity.setNetType(netType);
        customErrorLogEntity.setErrorType(errorType);
        customErrorLogEntity.setErrorMessage(errorMessage);
        customErrorLogEntity.setCreateTime(createTime);
        customErrorLogDao.save(customErrorLogEntity);

        log.info("--------[CustomErrorLogService]保存结束--------");

        return true;
    }

    /**
     * 查询某个时间段内的日志总数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return int
     */
    public int getCountByIdBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime) {
        return customErrorLogDao.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);
    }

    /**
     * 按小时间隔，获取各小时内的日志数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime, String projectIdentifier) {
        return customErrorLogDao.getLogCountByHours(startTime, endTime, projectIdentifier);
    }

    /**
     * 按天间隔，获取各天内的日志数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogCountByDays(Date startTime, Date endTime, String projectIdentifier) {
        return customErrorLogDao.getLogCountByDays(startTime, endTime, projectIdentifier);
    }

    /**
     * 获取时间间隔内的简易日志信息
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogListByCreateTimeAndProjectIdentifier(String projectIdentifier, Date startTime, Date endTime) {
        return customErrorLogDao.getLogListByCreateTimeAndProjectIdentifier(projectIdentifier, startTime, endTime);
    }

    /**
     * 获取时间间隔内的所有日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getAllLogsBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime) {
        return customErrorLogDao.findAllByProjectIdentifierAndCreateTimeBetween(projectIdentifier, startTime, endTime);
    }

    /**
     * 获取时间范围内的影响用户数
     *
     * @param startTime startTime
     * @param endTime   endTime
     * @return int
     */
    public int countDistinctCUuidByCreateTimeBetween(Date startTime, Date endTime) {
        return customErrorLogDao.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
    }

    /**
     * 获取时间范围内的日志数
     *
     * @param startTime startTime
     * @param endTime   endTime
     * @return int
     */
    public int countByCreateTimeBetween(Date startTime, Date endTime) {
        return customErrorLogDao.countByCreateTimeBetween(startTime, endTime);
    }
}
