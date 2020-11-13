package com.monitor.web.alarm.dto;

import lombok.Data;

@Data
public class SubscriberNotifyRecordDTO {

    /**
     * 报警记录id
     */
    private Long alarmRecordId;

    /**
     * 报警订阅方id
     */
    private Long subscriberId;

    /**
     * 通知状态，0-失败，1-成功
     */
    private int state;

    /**
     * 通知内容
     */
    private String content;
}
