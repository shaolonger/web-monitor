package com.monitor.web.alarm.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AlarmRecordWithRelatedInfoVO {

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
     * 预警名称
     */
    private String alarmName;
}
