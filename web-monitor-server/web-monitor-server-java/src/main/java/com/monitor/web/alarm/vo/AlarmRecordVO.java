package com.monitor.web.alarm.vo;

import com.monitor.web.alarm.entity.SubscriberNotifyRecordEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AlarmRecordVO {

    private Long id;

    /**
     * 预警规则id
     */
    private Long alarmId;

    /**
     * 报警内容，格式为JSON字符串
     */
    private String alarmData;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 通知记录
     */
    private List<SubscriberNotifyRecordEntity> notifyRecord;
}
