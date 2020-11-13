package com.monitor.web.alarm.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class AlarmRecordDTO {

    /**
     * 预警规则id
     */
    private Long alarmId;

    /**
     * 报警内容，格式为JSON字符串
     */
    private String alarmData;

    /**
     * 通知时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private Date noticeTime;

    /**
     * 预警状态，1-已创建未通知，2-已通知
     */
    private int state;
}
