package com.monitor.web.alarm.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriberNotifyRecordRelatedInfoVO {

    private Long id;

    /**
     * 通知状态，0-失败，1-成功
     */
    private int state;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 订阅类型，1-钉钉机器人，2-邮箱
     */
    private int category;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 预警名称
     */
    private String alarmName;
}
