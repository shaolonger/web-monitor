package com.monitor.web.alarm.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriberNotifyRecordVO {

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
     * 创建时间
     */
    private Date createTime;
}
