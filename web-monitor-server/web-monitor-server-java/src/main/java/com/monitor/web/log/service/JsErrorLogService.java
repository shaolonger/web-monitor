package com.monitor.web.log.service;

import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.log.dao.JsErrorLogDAO;
import com.monitor.web.log.entity.JsErrorLogEntity;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.utils.DateUtils;
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
import java.util.*;

@Service
public class JsErrorLogService extends ServiceBase {

    @Autowired
    private JsErrorLogDAO jsErrorLogDao;

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
        String errorType = request.getParameter("errorType");
        String errorMessage = request.getParameter("errorMessage");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from lms_js_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from lms_js_error_log t where 1=1");
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
        Page<JsErrorLogEntity> page = this.findPageBySqlAndParam(JsErrorLogEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<JsErrorLogEntity> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(page.getContent());
        return pageResultBase;
    }

    /**
     * 聚合查询
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
                "count(distinct t.c_uuid) as user_count, t.error_message from lms_js_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(*) from (select count(t.id) from lms_js_error_log t where 1=1");
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
     * @param jsErrorLogEntity jsErrorLog
     * @return Object
     */
    public boolean add(JsErrorLogEntity jsErrorLogEntity) {

        logger.info("--------[JsErrorLogService]保存开始--------");

        // 获取请求参数
        String logType = jsErrorLogEntity.getLogType();
        String cUuid = jsErrorLogEntity.getCUuid();
        Long bUid = jsErrorLogEntity.getBUid();
        String bUname = jsErrorLogEntity.getBUname();
        String pageUrl = jsErrorLogEntity.getPageUrl();
        String pageKey = jsErrorLogEntity.getPageKey();
        String deviceName = jsErrorLogEntity.getDeviceName();
        String os = jsErrorLogEntity.getOs();
        String osVersion = jsErrorLogEntity.getOsVersion();
        String browserName = jsErrorLogEntity.getBrowserName();
        String browserVersion = jsErrorLogEntity.getBrowserVersion();
        String ipAddress = jsErrorLogEntity.getIpAddress();
        String netType = jsErrorLogEntity.getNetType();
        String errorType = jsErrorLogEntity.getErrorType();
        String errorMessage = jsErrorLogEntity.getErrorMessage();
        String errorStack = jsErrorLogEntity.getErrorStack();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        jsErrorLogEntity.setLogType(logType);
        jsErrorLogEntity.setCUuid(cUuid);
        jsErrorLogEntity.setBUid(bUid);
        jsErrorLogEntity.setBUname(bUname);
        jsErrorLogEntity.setPageUrl(pageUrl);
        jsErrorLogEntity.setPageKey(pageKey);
        jsErrorLogEntity.setDeviceName(deviceName);
        jsErrorLogEntity.setOs(os);
        jsErrorLogEntity.setOsVersion(osVersion);
        jsErrorLogEntity.setBrowserName(browserName);
        jsErrorLogEntity.setBrowserVersion(browserVersion);
        jsErrorLogEntity.setIpAddress(ipAddress);
        jsErrorLogEntity.setNetType(netType);
        jsErrorLogEntity.setErrorType(errorType);
        jsErrorLogEntity.setErrorMessage(errorMessage);
        jsErrorLogEntity.setErrorStack(errorStack);
        jsErrorLogEntity.setCreateTime(createTime);
        jsErrorLogDao.save(jsErrorLogEntity);

        logger.info("--------[JsErrorLogService]保存结束--------");

        return true;
    }

    /**
     * 新增-LogService通用
     *
     * @param request request
     * @return boolean
     */
    public boolean add(HttpServletRequest request) throws Exception {

        logger.info("--------[JsErrorLogService]保存开始--------");

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
        String errorType = request.getParameter("errorType");
        String errorMessage = request.getParameter("errorMessage");
        String errorStack = request.getParameter("errorStack");

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
        if (errorMessage == null || errorMessage.isEmpty()) {
            throw new Exception("errorMessage不能为空");
        }
        if (errorStack == null || errorStack.isEmpty()) {
            throw new Exception("errorStack不能为空");
        }

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        JsErrorLogEntity jsErrorLogEntity = new JsErrorLogEntity();
        jsErrorLogEntity.setProjectIdentifier(projectIdentifier);
        jsErrorLogEntity.setLogType(logType);
        jsErrorLogEntity.setCUuid(cUuid);
        jsErrorLogEntity.setBUid(bUid);
        jsErrorLogEntity.setBUname(bUname);
        jsErrorLogEntity.setPageUrl(pageUrl);
        jsErrorLogEntity.setPageKey(pageKey);
        jsErrorLogEntity.setDeviceName(deviceName);
        jsErrorLogEntity.setOs(os);
        jsErrorLogEntity.setOsVersion(osVersion);
        jsErrorLogEntity.setBrowserName(browserName);
        jsErrorLogEntity.setBrowserVersion(browserVersion);
        jsErrorLogEntity.setIpAddress(ipAddress);
        jsErrorLogEntity.setNetType(netType);
        jsErrorLogEntity.setErrorType(errorType);
        jsErrorLogEntity.setErrorMessage(errorMessage);
        jsErrorLogEntity.setErrorStack(errorStack);
        jsErrorLogEntity.setCreateTime(createTime);
        jsErrorLogDao.save(jsErrorLogEntity);

        logger.info("--------[JsErrorLogService]保存结束--------");

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
        return jsErrorLogDao.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);
    }

    /**
     * 按小时间隔，获取各小时内的日志数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime, String projectIdentifier) {
        return jsErrorLogDao.getLogCountByHours(startTime, endTime, projectIdentifier);
    }

    /**
     * 按天间隔，获取各天内的日志数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogCountByDays(Date startTime, Date endTime, String projectIdentifier) {
        return jsErrorLogDao.getLogCountByDays(startTime, endTime, projectIdentifier);
    }

    /**
     * 获取时间间隔内的简易日志信息
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogListByCreateTimeAndProjectIdentifier(String projectIdentifier, Date startTime, Date endTime) {
        return jsErrorLogDao.getLogListByCreateTimeAndProjectIdentifier(projectIdentifier, startTime, endTime);
    }

    /**
     * 获取时间间隔内的所有日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getAllLogsBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime) {
        return jsErrorLogDao.findAllByProjectIdentifierAndCreateTimeBetween(projectIdentifier, startTime, endTime);
    }

    /**
     * 获取时间范围内的影响用户数
     *
     * @param startTime startTime
     * @param endTime   endTime
     * @return int
     */
    public int countDistinctCUuidByCreateTimeBetween(Date startTime, Date endTime) {
        return jsErrorLogDao.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
    }

    /**
     * 获取时间范围内的日志数
     *
     * @param startTime startTime
     * @param endTime   endTime
     * @return int
     */
    public int countByCreateTimeBetween(Date startTime, Date endTime) {
        return jsErrorLogDao.countByCreateTimeBetween(startTime, endTime);
    }
}
