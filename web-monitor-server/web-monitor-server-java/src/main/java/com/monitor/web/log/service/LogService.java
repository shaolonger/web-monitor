package com.monitor.web.log.service;

import com.monitor.web.alarm.bean.AlarmRuleItemBean;
import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.log.entity.CustomErrorLogEntity;
import com.monitor.web.log.entity.HttpErrorLogEntity;
import com.monitor.web.log.entity.JsErrorLogEntity;
import com.monitor.web.log.entity.ResourceLoadErrorLogEntity;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.utils.DateUtils;
import com.monitor.web.common.service.ServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class LogService extends ServiceBase {

    @Autowired
    JsErrorLogService jsErrorLogService;
    @Autowired
    HttpErrorLogService httpErrorLogService;
    @Autowired
    ResourceLoadErrorLogService resourceLoadErrorLogService;
    @Autowired
    CustomErrorLogService customErrorLogService;
    @Autowired
    ClientUserService clientUserService;

    /**
     * 打点日志上传
     *
     * @param request request
     * @return Object
     */
    public Object add(HttpServletRequest request) throws Exception {
        String logType = request.getParameter("logType");
        if (logType == null || logType.isEmpty()) throw new Exception("logType不能为空");
        switch (logType) {
            case "JS_ERROR":
                return jsErrorLogService.add(request);
            case "HTTP_ERROR":
                return httpErrorLogService.add(request);
            case "RESOURCE_LOAD_ERROR":
                return resourceLoadErrorLogService.add(request);
            case "CUSTOM_ERROR":
                return customErrorLogService.add(request);
            default:
                throw new Exception("logType无效");
        }
    }

    /**
     * 高级查询-多条件
     *
     * @param request request
     * @return Object
     */
    public Object getLogListByConditions(HttpServletRequest request) throws Exception {

        // 获取请求参数
        int pageNum = DataConvertUtils.strToInt(request.getParameter("pageNum"));
        int pageSize = DataConvertUtils.strToInt(request.getParameter("pageSize"));
        String projectIdentifier = request.getParameter("projectIdentifier");
        String logType = request.getParameter("logType");
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        String conditionListStr = request.getParameter("conditionList");
        if (conditionListStr == null || conditionListStr.isEmpty()) throw new Exception("conditionList参数不合规");
        List<HashMap<String, String>> conditionList = DataConvertUtils.jsonStrToObject(conditionListStr, List.class);

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();

        HashMap<String, String> tableNameMap = new HashMap<String, String>() {{
            put("jsErrorLog", "lms_js_error_log");
            put("httpErrorLog", "lms_http_error_log");
            put("resourceLoadErrorLog", "lms_resource_load_error_log");
            put("customErrorLog", "lms_custom_error_log");
        }};
        String tableName = tableNameMap.get(logType);
        if (tableName == null) throw new Exception("logType参数不合规");

        HashMap<String, Class> classMap = new HashMap<String, Class>() {{
            put("jsErrorLog", JsErrorLogEntity.class);
            put("httpErrorLog", HttpErrorLogEntity.class);
            put("resourceLoadErrorLog", ResourceLoadErrorLogEntity.class);
            put("customErrorLog", CustomErrorLogEntity.class);
        }};
        Class aClass = classMap.get(logType);

        StringBuilder dataSqlBuilder = new StringBuilder("select * from ").append(tableName).append(" t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(id) from ").append(tableName).append(" t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 项目标识
        if (projectIdentifier != null && !projectIdentifier.isEmpty()) {
            paramSqlBuilder.append(" and t.project_identifier = :projectIdentifier");
            paramMap.put("projectIdentifier", projectIdentifier);
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

        if (conditionList.size() > 0) {
            conditionList.forEach(condition -> {
                String key = condition.get("key");
                String value = condition.get("value");
                String op = condition.get("op");
                setParamSqlBuilder(paramSqlBuilder, paramMap, key, value, op, "t");
            });
        }

        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page page = this.findPageBySqlAndParam(aClass, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

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
     * 根据预警规则，判断是否已超过预警阈值
     *
     * @param tableName         tableName
     * @param alarmRuleItemBean alarmRuleItem
     * @return HashMap
     */
    public HashMap<String, Object> checkIsExceedAlarmThreshold(String tableName, AlarmRuleItemBean alarmRuleItemBean) {

        int timeSpan = alarmRuleItemBean.getTimeSpan();
        int interval = alarmRuleItemBean.getInterval();
        String ind = alarmRuleItemBean.getInd();
        String op = alarmRuleItemBean.getOp();
        String agg = alarmRuleItemBean.getAgg();
        float val = alarmRuleItemBean.getVal();

        // 当聚合维度为平均时，gap用于计算平均值
        int gap = timeSpan / interval;

        Date endTime = new Date();
        Date startTime = DateUtils.getDateBeforeOrAfterByMinutes(endTime, -timeSpan);

        // 返回结果
        HashMap<String, Object> resultMap = new HashMap<String, Object>() {
        };
        boolean isExceedAlarmThreshold = false;
        String targetInd = null;
        float actualValue = 0F;

        // ------------------------ 影响用户数 ------------------------
        if ("uvCount".equals(ind)) {
            targetInd = "影响用户数";
            int uvCount = 0;
            switch (tableName) {
                case "lms_js_error_log":
                    uvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_http_error_log":
                    uvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_resource_load_error_log":
                    uvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_custom_error_log":
                    uvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
            }
            if (">".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = uvCount > val;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = (float) (uvCount / gap) > val;
                }
                actualValue = uvCount;
            }
            if ("<".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = uvCount < val;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = (float) (uvCount / gap) < val;
                }
                actualValue = uvCount;
            }
            // 环比昨天上涨、下跌
            if ("d_up".equals(op) || "d_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -1);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -1);
                int preUvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        preUvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        preUvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        preUvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        preUvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                if ("d_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = uvCount > preUvCount;
                        actualValue = uvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = uvCount / gap > preUvCount;
                        actualValue = (float) uvCount / gap;
                    }
                }
                if ("d_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = uvCount < preUvCount;
                        actualValue = uvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = uvCount / gap < preUvCount;
                        actualValue = (float) uvCount / gap;
                    }
                }
            }
            // 环比上周上涨、下跌
            if ("w_up".equals(op) || "w_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -7);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -7);
                int preUvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        preUvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        preUvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        preUvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        preUvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                if ("w_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = uvCount > preUvCount;
                        actualValue = uvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = uvCount / gap > preUvCount;
                        actualValue = (float) uvCount / gap;
                    }
                }
                if ("w_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = uvCount < preUvCount;
                        actualValue = uvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = uvCount / gap < preUvCount;
                        actualValue = (float) uvCount / gap;
                    }
                }
            }
        }

        // ------------------------ 影响用户率 ------------------------
        if ("uvRate".equals(ind)) {
            targetInd = "影响用户率";
            int uvCount = 0;
            int uvTotal = clientUserService.countDistinctCUuid();
            switch (tableName) {
                case "lms_js_error_log":
                    uvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_http_error_log":
                    uvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_resource_load_error_log":
                    uvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_custom_error_log":
                    uvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
            }
            float uvRate = (float) uvCount / uvTotal * 100;
            if (">".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = uvRate > val;
                    actualValue = uvRate;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = uvRate / gap > val;
                    actualValue = uvRate / gap;
                }
            }
            if ("<".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = uvRate < val;
                    actualValue = uvRate;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = uvRate / gap < val;
                    actualValue = uvRate / gap;
                }
            }
            // 环比昨天上涨、下跌
            if ("d_up".equals(op) || "d_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -1);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -1);
                int preUvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        preUvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        preUvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        preUvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        preUvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                float preUvRate = (float) preUvCount / uvTotal;
                if ("d_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate > uvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap > uvRate;
                        actualValue = preUvRate / gap;
                    }
                }
                if ("d_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate < uvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap < uvRate;
                        actualValue = preUvRate / gap;
                    }
                }
            }
            // 环比上周上涨、下跌
            if ("w_up".equals(op) || "w_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -7);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -7);
                int preUvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        preUvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        preUvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        preUvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        preUvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                float preUvRate = (float) preUvCount / uvTotal;
                if ("w_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate > uvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap > uvRate;
                        actualValue = preUvRate / gap;
                    }
                }
                if ("w_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate < uvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap < uvRate;
                        actualValue = preUvRate / gap;
                    }
                }
            }
        }

        // ------------------------ 人均异常次数 ------------------------
        if ("perPV".equals(ind)) {
            targetInd = "人均异常次数";
            int pvCount = 0;
            int uvTotal = clientUserService.countDistinctCUuid();
            switch (tableName) {
                case "lms_js_error_log":
                    pvCount = jsErrorLogService.countByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_http_error_log":
                    pvCount = httpErrorLogService.countByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_resource_load_error_log":
                    pvCount = resourceLoadErrorLogService.countByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_custom_error_log":
                    pvCount = customErrorLogService.countByCreateTimeBetween(startTime, endTime);
                    break;
            }
            float pvRate = (float) pvCount / uvTotal;
            if (">".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = pvRate > val;
                    actualValue = pvRate;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = pvRate / gap > val;
                    actualValue = pvRate / gap;
                }
            }
            if ("<".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = pvRate < val;
                    actualValue = pvRate;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = pvRate / gap < val;
                    actualValue = pvRate / gap;
                }
            }
            // 环比昨天上涨、下跌
            if ("d_up".equals(op) || "d_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -1);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -1);
                int prePvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        prePvCount = jsErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        prePvCount = httpErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        prePvCount = resourceLoadErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        prePvCount = customErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                float preUvRate = (float) prePvCount / uvTotal;
                if ("d_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate > pvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap > pvRate;
                        actualValue = preUvRate / gap;
                    }
                }
                if ("d_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate < pvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap < pvRate;
                        actualValue = preUvRate / gap;
                    }
                }
            }
            // 环比上周上涨、下跌
            if ("w_up".equals(op) || "w_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -7);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -7);
                int prePvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        prePvCount = jsErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        prePvCount = httpErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        prePvCount = resourceLoadErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        prePvCount = customErrorLogService.countByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                float preUvRate = (float) prePvCount / uvTotal;
                if ("w_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate > pvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap > pvRate;
                        actualValue = preUvRate / gap;
                    }
                }
                if ("w_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate < pvRate;
                        actualValue = preUvRate;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = preUvRate / gap < pvRate;
                        actualValue = preUvRate / gap;
                    }
                }
            }
        }

        // ------------------------ 新增异常数 ------------------------
        if ("newPV".equals(ind)) {
            targetInd = "新增异常数";
            int pvCount = 0;
            switch (tableName) {
                case "lms_js_error_log":
                    pvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_http_error_log":
                    pvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_resource_load_error_log":
                    pvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
                case "lms_custom_error_log":
                    pvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(startTime, endTime);
                    break;
            }
            if (">".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = pvCount > val;
                    actualValue = pvCount;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = (float) (pvCount / gap) > val;
                    actualValue = (float) pvCount / gap;
                }
            }
            if ("<".equals(op)) {
                if ("count".equals(agg)) {
                    isExceedAlarmThreshold = pvCount < val;
                    actualValue = pvCount;
                }
                if ("avg".equals(agg)) {
                    isExceedAlarmThreshold = (float) (pvCount / gap) < val;
                    actualValue = (float) pvCount / gap;
                }
            }
            // 环比昨天上涨、下跌
            if ("d_up".equals(op) || "d_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -1);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -1);
                int prePvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        prePvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        prePvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        prePvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        prePvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                if ("d_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = pvCount > prePvCount;
                        actualValue = pvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = pvCount / gap > prePvCount;
                        actualValue = (float) pvCount / gap;
                    }
                }
                if ("d_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = pvCount < prePvCount;
                        actualValue = pvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = pvCount / gap < prePvCount;
                        actualValue = (float) pvCount / gap;
                    }
                }
            }
            // 环比上周上涨、下跌
            if ("w_up".equals(op) || "w_down".equals(op)) {
                Date preStartTime = DateUtils.getDateBeforeOrAfterByDays(startTime, -7);
                Date preEndTime = DateUtils.getDateBeforeOrAfterByDays(endTime, -7);
                int prePvCount = 0;
                switch (tableName) {
                    case "lms_js_error_log":
                        prePvCount = jsErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_http_error_log":
                        prePvCount = httpErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_resource_load_error_log":
                        prePvCount = resourceLoadErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                    case "lms_custom_error_log":
                        prePvCount = customErrorLogService.countDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime);
                        break;
                }
                if ("w_up".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = pvCount > prePvCount;
                        actualValue = pvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = pvCount / gap > prePvCount;
                        actualValue = (float) pvCount / gap;
                    }
                }
                if ("w_down".equals(op)) {
                    if ("count".equals(agg)) {
                        isExceedAlarmThreshold = pvCount < prePvCount;
                        actualValue = pvCount;
                    }
                    if ("avg".equals(agg)) {
                        isExceedAlarmThreshold = pvCount / gap < prePvCount;
                        actualValue = (float) pvCount / gap;
                    }
                }
            }
        }

        // 设置预警检查结果
        resultMap.put("isExceedAlarmThreshold", isExceedAlarmThreshold);
        resultMap.put("targetInd", targetInd);
        resultMap.put("actualValue", actualValue);
        resultMap.put("thresholdValue", val);
        resultMap.put("startTime", startTime);
        resultMap.put("endTime", endTime);

        return resultMap;
    }

    /**
     * 设置动态查询语句
     *
     * @param paramSqlBuilder 动态sql语句
     * @param paramMap        参数组
     * @param key             字段名
     * @param value           字段值
     * @param op              操作符
     * @param shortTableName  表名
     */
    private void setParamSqlBuilder(StringBuilder paramSqlBuilder, Map<String, Object> paramMap, String key, String value, String op, String shortTableName) {
        // 表名缩写默认为"t"
        String tableName = (shortTableName == null || shortTableName.isEmpty()) ? "t" : shortTableName;

        if (key == null || key.isEmpty()) return;
        if (value == null || value.isEmpty()) return;
        if (op == null || op.isEmpty()) return;

        // 可以直接拼接的操作符
        ArrayList<String> canDirectAppendOpList = new ArrayList<String>() {{
            add("=");
            add(">");
            add(">=");
            add("<");
            add("<=");
            add("!=");
        }};

        if (canDirectAppendOpList.contains(op)) {
            paramSqlBuilder.append(" and ").append(tableName).append(".").append(key).append(" ").append(op).append(" ").append(":").append(key);
            paramMap.put(key, value);
        } else {
            // 不可以直接拼接的操作符
            // 通配符查询，即模糊查询
            if ("wildcard".equals(op)) {
                paramSqlBuilder.append(" and ").append(tableName).append(".").append(key).append(" ").append("like").append(" ").append(":").append(key);
                paramMap.put(key, "%" + value + "%");
            }
        }
    }
}
