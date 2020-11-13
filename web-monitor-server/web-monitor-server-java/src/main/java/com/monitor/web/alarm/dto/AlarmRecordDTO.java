package com.monitor.web.alarm.dto;

import lombok.Data;

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
}
