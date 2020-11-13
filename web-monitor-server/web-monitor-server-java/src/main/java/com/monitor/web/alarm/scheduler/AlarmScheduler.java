package com.monitor.web.alarm.scheduler;

import com.monitor.web.alarm.bean.AlarmRuleBean;
import com.monitor.web.alarm.bean.AlarmRuleItemBean;
import com.monitor.web.alarm.entity.AlarmEntity;
import com.monitor.web.log.service.LogService;
import com.monitor.web.utils.DataConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("AlarmScheduler")
@Slf4j
public class AlarmScheduler {

    @Autowired
    LogService logService;

    private static final String beanName = "AlarmScheduler";

    private static final String methodName = "startScheduler";

    private static final String cronExpression = "*/10 * * * * ?";

    public String getBeanName() {
        return beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 预警定时任务
     *
     * @param object object
     */
    public void startScheduler(Object object) {
        AlarmEntity alarmEntity = (AlarmEntity) object;
        // 获取预警规则
        String rule = alarmEntity.getRule();
        try {
            AlarmRuleBean alarmRuleBean = DataConvertUtils.jsonStrToObject(rule, AlarmRuleBean.class);

            // 过滤条件
            int category = alarmEntity.getCategory();
            HashMap<Integer, String> tableNameMap = new HashMap<Integer, String>() {{
                put(1, "lms_js_error_log");
                put(2, "lms_http_error_log");
                put(3, "lms_resource_load_error_log");
                put(4, "lms_custom_error_log");
            }};
            List<AlarmRuleItemBean> alarmRuleItemBeanList = alarmRuleBean.getRules();
            ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();

            // 根据category过滤条件，查询对应的日志表
            if (category == 0) {
                ArrayList<HashMap<String, Object>> tempList = new ArrayList<>();
                tableNameMap.values().forEach(tableName -> {
                    alarmRuleItemBeanList.forEach(alarmRuleItemBean -> {
                        tempList.add(logService.checkIsExceedAlarmThreshold(tableName, alarmRuleItemBean));
                    });
                });
                // 聚合分析
                this.setResultListByTempList(tempList, resultList);
            } else {
                String tableName = tableNameMap.get(category);
                alarmRuleItemBeanList.forEach(alarmRuleItemBean -> {
                    resultList.add(logService.checkIsExceedAlarmThreshold(tableName, alarmRuleItemBean));
                });
            }

            // 根据条件规则判断是否触发报警
            String op = alarmRuleBean.getOp();
            boolean isAnd = true;
            boolean isOr = false;
            for (HashMap<String, Object> map : resultList) {
                boolean isExceedAlarmThreshold = (boolean) map.get("isExceedAlarmThreshold");
                isAnd = isAnd && isExceedAlarmThreshold;
                isOr = isOr || isExceedAlarmThreshold;
                if (isExceedAlarmThreshold) {
                    // 整合报警内容
                    String alarmContent = "[预警指标]" + map.get("targetInd") + ", " + "[预警阈值]" +
                            map.get("thresholdValue") + ", " + "[实际值]" + map.get("actualValue");
                    map.put("alarmContent", alarmContent);
                }
            }
            if ("&&".equals(op) && isAnd) {
                // 触发预警条件，添加报警记录
                log.info("[预警定时任务]预警名称：{}，报警内容：{}", alarmEntity.getName(), resultList);
                // TODO 记录报警记录，同时触发报警通知
            }
            if ("||".equals(op) && isOr) {
                // 触发预警条件，添加报警记录
                log.info("[预警定时任务]预警名称：{}，报警内容：{}", alarmEntity.getName(), resultList);
                // TODO 记录报警记录，同时触发报警通知
            }
        } catch (Exception e) {
            log.error("[预警定时任务]预警名称：{}，异常：{}", alarmEntity.getName(), e.getStackTrace());
        }
    }

    /**
     * 当category为0时，即选择的过滤条件为全部，此时需要对各个表的计算结果进行聚合分析
     *
     * @param tempList   tempList
     * @param resultList resultList
     */
    private void setResultListByTempList(ArrayList<HashMap<String, Object>> tempList, ArrayList<HashMap<String, Object>> resultList) {
        tempList.forEach(map -> {
            HashMap<String, Object> resultMap = resultList
                    .stream()
                    .filter(item -> map.get("targetInd").equals(item.get("targetInd")))
                    .findAny()
                    .orElse(null);
            if (resultMap == null) {
                resultMap = new HashMap<>();
                resultMap.put("isExceedAlarmThreshold", map.get("isExceedAlarmThreshold"));
                resultMap.put("targetInd", map.get("targetInd"));
                resultMap.put("actualValue", map.get("actualValue"));
                resultMap.put("thresholdValue", map.get("thresholdValue"));
                resultList.add(resultMap);
            } else {
                float thresholdValue = (float) resultMap.get("thresholdValue");
                float oldActualValue = (float) resultMap.get("actualValue");
                float newActualValue = oldActualValue + (float) map.get("actualValue");
                resultMap.put("actualValue", newActualValue);
                resultMap.put("isExceedAlarmThreshold", newActualValue > thresholdValue);
            }
        });
    }
}
