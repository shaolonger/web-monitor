package com.shaolonger.webmonitorserver.log.service;

import com.shaolonger.webmonitorserver.common.api.PageResultBase;
import com.shaolonger.webmonitorserver.common.service.ServiceBase;
import com.shaolonger.webmonitorserver.log.entity.CustomErrorLogEntity;
import com.shaolonger.webmonitorserver.log.entity.HttpErrorLogEntity;
import com.shaolonger.webmonitorserver.log.entity.JsErrorLogEntity;
import com.shaolonger.webmonitorserver.log.entity.ResourceLoadErrorLogEntity;
import com.shaolonger.webmonitorserver.utils.DataConvertUtils;
import com.shaolonger.webmonitorserver.utils.DateUtils;
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
