package com.monitor.web.log.service;

import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.log.dao.HttpErrorLogDao;
import com.monitor.web.log.entity.HttpErrorLogEntity;
import com.monitor.web.log.vo.StatisticRecordVO;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.utils.DateUtils;
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
public class HttpErrorLogService extends ServiceBase {

    @Autowired
    private HttpErrorLogDao httpErrorLogDao;

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
        if (bUname != null && !bUname.isEmpty()) {
            paramSqlBuilder.append(" and t.b_uname like :bUname");
            paramMap.put("bUname", "%" + bUname + "%");
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
        Page<HttpErrorLogEntity> page = this.findPageBySqlAndParam(HttpErrorLogEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<HttpErrorLogEntity> pageResultBase = new PageResultBase<>();
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
        String httpType = request.getParameter("httpType");
        String httpUrlComplete = request.getParameter("httpUrlComplete");
        String httpUrlShort = request.getParameter("httpUrlShort");
        String status = request.getParameter("status");
        String statusText = request.getParameter("statusText");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select count(t.id) as count, max(t.create_time) as latest_create_time, " +
                "count(distinct t.c_uuid) as user_count, t.http_url_complete from lms_http_error_log t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(*) from (select count(t.id) from lms_http_error_log t where 1=1");
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
        dataSqlBuilder.append(paramSqlBuilder).append(" group by t.http_url_complete order by count desc");
        countSqlBuilder.append(paramSqlBuilder.append(" group by t.http_url_complete) s"));
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
     * 新增-LogService通用
     *
     * @param httpErrorLogEntity httpErrorLog
     * @return Object
     */
    public boolean add(HttpErrorLogEntity httpErrorLogEntity) {

        logger.info("--------[HttpErrorLogService]保存开始--------");

        // 获取请求参数
        String logType = httpErrorLogEntity.getLogType();
        String cUuid = httpErrorLogEntity.getCUuid();
        Long bUid = httpErrorLogEntity.getBUid();
        String bUname = httpErrorLogEntity.getBUname();
        String pageUrl = httpErrorLogEntity.getPageUrl();
        String pageKey = httpErrorLogEntity.getPageKey();
        String deviceName = httpErrorLogEntity.getDeviceName();
        String os = httpErrorLogEntity.getOs();
        String osVersion = httpErrorLogEntity.getOsVersion();
        String browserName = httpErrorLogEntity.getBrowserName();
        String browserVersion = httpErrorLogEntity.getBrowserVersion();
        String ipAddress = httpErrorLogEntity.getIpAddress();
        String netType = httpErrorLogEntity.getNetType();
        String httpType = httpErrorLogEntity.getHttpType();
        String httpUrlComplete = httpErrorLogEntity.getHttpUrlComplete();
        String httpUrlShort = httpErrorLogEntity.getHttpUrlShort();
        String status = httpErrorLogEntity.getStatus();
        String statusText = httpErrorLogEntity.getStatusText();
        String resTime = httpErrorLogEntity.getResTime();

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        httpErrorLogEntity.setLogType(logType);
        httpErrorLogEntity.setCUuid(cUuid);
        httpErrorLogEntity.setBUid(bUid);
        httpErrorLogEntity.setBUname(bUname);
        httpErrorLogEntity.setPageUrl(pageUrl);
        httpErrorLogEntity.setPageKey(pageKey);
        httpErrorLogEntity.setDeviceName(deviceName);
        httpErrorLogEntity.setOs(os);
        httpErrorLogEntity.setOsVersion(osVersion);
        httpErrorLogEntity.setBrowserName(browserName);
        httpErrorLogEntity.setBrowserVersion(browserVersion);
        httpErrorLogEntity.setIpAddress(ipAddress);
        httpErrorLogEntity.setNetType(netType);
        httpErrorLogEntity.setHttpType(httpType);
        httpErrorLogEntity.setHttpUrlComplete(httpUrlComplete);
        httpErrorLogEntity.setHttpUrlShort(httpUrlShort);
        httpErrorLogEntity.setStatus(status);
        httpErrorLogEntity.setStatusText(statusText);
        httpErrorLogEntity.setResTime(resTime);
        httpErrorLogEntity.setCreateTime(createTime);
        httpErrorLogDao.save(httpErrorLogEntity);

        logger.info("--------[HttpErrorLogService]保存结束--------");

        return true;
    }

    /**
     * 新增
     *
     * @param request request
     * @return Object
     */
    public boolean add(HttpServletRequest request) throws Exception {

        logger.info("--------[HttpErrorLogService]保存开始--------");

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
        String httpType = request.getParameter("httpType");
        String httpUrlComplete = request.getParameter("httpUrlComplete");
        String httpUrlShort = request.getParameter("httpUrlShort");
        String status = request.getParameter("status");
        String statusText = request.getParameter("statusText");
        String resTime = request.getParameter("resTime");

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
        if (httpUrlComplete == null || httpUrlComplete.isEmpty()) {
            throw new Exception("httpUrlComplete不能为空");
        }
        if (status == null || status.isEmpty()) {
            throw new Exception("status不能为空");
        }

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        HttpErrorLogEntity httpErrorLogEntity = new HttpErrorLogEntity();
        httpErrorLogEntity.setProjectIdentifier(projectIdentifier);
        httpErrorLogEntity.setLogType(logType);
        httpErrorLogEntity.setCUuid(cUuid);
        httpErrorLogEntity.setBUid(bUid);
        httpErrorLogEntity.setBUname(bUname);
        httpErrorLogEntity.setPageUrl(pageUrl);
        httpErrorLogEntity.setPageKey(pageKey);
        httpErrorLogEntity.setDeviceName(deviceName);
        httpErrorLogEntity.setOs(os);
        httpErrorLogEntity.setOsVersion(osVersion);
        httpErrorLogEntity.setBrowserName(browserName);
        httpErrorLogEntity.setBrowserVersion(browserVersion);
        httpErrorLogEntity.setIpAddress(ipAddress);
        httpErrorLogEntity.setNetType(netType);
        httpErrorLogEntity.setHttpType(httpType);
        httpErrorLogEntity.setHttpUrlComplete(httpUrlComplete);
        httpErrorLogEntity.setHttpUrlShort(httpUrlShort);
        httpErrorLogEntity.setStatus(status);
        httpErrorLogEntity.setStatusText(statusText);
        httpErrorLogEntity.setResTime(resTime);
        httpErrorLogEntity.setCreateTime(createTime);
        httpErrorLogDao.save(httpErrorLogEntity);

        logger.info("--------[HttpErrorLogService]保存结束--------");

        return true;
    }

    /**
     * 查询某个时间段内的日志总数
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return int
     */
    public int getCountByIdBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime) {
        return httpErrorLogDao.getCountByIdBetweenStartTimeAndEndTime(projectIdentifier, startTime, endTime);
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

    /**
     * 获取时间间隔内的简易日志信息
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getLogListByCreateTimeAndProjectIdentifier(String projectIdentifier, Date startTime, Date endTime) {
        return httpErrorLogDao.getLogListByCreateTimeAndProjectIdentifier(projectIdentifier, startTime, endTime);
    }

    /**
     * 获取时间间隔内的所有日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List
     */
    public List<Map<String, Object>> getAllLogsBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime) {
        return httpErrorLogDao.findAllByProjectIdentifierAndCreateTimeBetween(projectIdentifier, startTime, endTime);
    }
}
