package com.monitor.web.alarm.scheduler;

import com.monitor.web.alarm.bean.AlarmRuleBean;
import com.monitor.web.alarm.bean.AlarmRuleItemBean;
import com.monitor.web.alarm.dto.AlarmRecordDTO;
import com.monitor.web.alarm.dto.SubscriberNotifyRecordDTO;
import com.monitor.web.alarm.entity.AlarmEntity;
import com.monitor.web.alarm.entity.SubscriberEntity;
import com.monitor.web.alarm.service.AlarmRecordService;
import com.monitor.web.alarm.service.SubscriberNotifyRecordService;
import com.monitor.web.alarm.service.SubscriberService;
import com.monitor.web.log.service.LogService;
import com.monitor.web.utils.DataConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component("AlarmScheduler")
@Slf4j
public class AlarmScheduler {

    @Autowired
    LogService logService;
    @Autowired
    SubscriberService subscriberService;
    @Autowired
    AlarmRecordService alarmRecordService;
    @Autowired
    SubscriberNotifyRecordService subscriberNotifyRecordService;

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
            if (("&&".equals(op) && isAnd) || ("||".equals(op) && isOr)) {
                // 触发预警条件，添加报警记录
//                log.info("[预警定时任务]预警名称：{}，报警内容：{}", alarmEntity.getName(), resultList);
                this.saveAlarmRecordAndNotifyAllSubscribers(alarmEntity, resultList);
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

    /**
     * 保存报警记录，同时通知所有报警订阅方
     *
     * @param alarmEntity alarmEntity
     * @param resultList  resultList
     */
    @Transactional(rollbackOn = {Exception.class})
    protected void saveAlarmRecordAndNotifyAllSubscribers(AlarmEntity alarmEntity, ArrayList<HashMap<String, Object>> resultList) {
        List<SubscriberEntity> subscriberEntityList = subscriberService.getAllByAlarmId(alarmEntity.getId());

        // 保存报警记录
        try {
            String alarmData = DataConvertUtils.objectToJsonStr(resultList);
            AlarmRecordDTO alarmRecordDTO = new AlarmRecordDTO();
            alarmRecordDTO.setAlarmId(alarmEntity.getId());
            alarmRecordDTO.setAlarmData(alarmData);
            long alarmRecordId = alarmRecordService.add(alarmRecordDTO);

            // 通知所有报警订阅方
            this.notifyAllSubscribers(alarmRecordId, subscriberEntityList);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 通知所有报警订阅方
     *
     * @param alarmRecordId        alarmRecordId
     * @param subscriberEntityList subscriberEntityList
     */
    private void notifyAllSubscribers(long alarmRecordId, List<SubscriberEntity> subscriberEntityList) {
        for (SubscriberEntity subscriberEntity : subscriberEntityList) {
            int isActive = subscriberEntity.getIsActive();
            if (isActive == 1) {
                String subscriber = subscriberEntity.getSubscriber();
                String[] subscriberList = subscriber.split(",");

                SubscriberNotifyRecordDTO subscriberNotifyRecordDTO = new SubscriberNotifyRecordDTO();
                Long subscriberId = subscriberEntity.getId();
                subscriberNotifyRecordDTO.setAlarmRecordId(alarmRecordId);
                subscriberNotifyRecordDTO.setSubscriberId(subscriberId);

                int category = subscriberEntity.getCategory();
                if (category == 1) {
                    // 钉钉机器人
                    subscriberNotifyRecordDTO.setState(2); // TODO 发送通知，并标记发送状态
                    subscriberNotifyRecordService.add(subscriberNotifyRecordDTO);
                } else if (category == 2) {
                    // 邮箱
                    subscriberNotifyRecordDTO.setState(2); // TODO 发送通知，并标记发送状态
                    subscriberNotifyRecordService.add(subscriberNotifyRecordDTO);
                }
            }
        }
    }
}
