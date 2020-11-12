package com.monitor.web.alarm.scheduler;

import com.monitor.web.alarm.bean.AlarmRuleBean;
import com.monitor.web.alarm.bean.AlarmRuleItemBean;
import com.monitor.web.alarm.entity.AlarmEntity;
import com.monitor.web.log.service.LogService;
import com.monitor.web.utils.DataConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("AlarmScheduler")
public class AlarmScheduler {

    @Autowired
    LogService logService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
                tableNameMap.values().forEach(tableName -> {
                    alarmRuleItemBeanList.forEach(alarmRuleItemBean -> {
                        resultList.add(logService.checkIsExceedAlarmThreshold(tableName, alarmRuleItemBean));
                    });
                });
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
                logger.info("[预警定时任务]预警名称：{}，报警内容：{}", alarmEntity.getName(), resultList);
                // TODO 记录报警记录，同时触发报警通知
            }
            if ("||".equals(op) && isOr) {
                // 触发预警条件，添加报警记录
                logger.info("[预警定时任务]预警名称：{}，报警内容：{}", alarmEntity.getName(), resultList);
                // TODO 记录报警记录，同时触发报警通知
            }
        } catch (Exception e) {
            logger.error("[预警定时任务]预警名称：{}，异常：{}", alarmEntity.getName(), e.getStackTrace());
        }
    }
}
