package com.monitor.web.alarm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ams_subscriber_notify_record")
public class SubscriberNotifyRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 报警记录id
     */
    @Column(nullable = false)
    private Long alarmRecordId;

    /**
     * 报警订阅方id
     */
    @Column(nullable = false)
    private Long subscriberId;

    /**
     * 通知状态，0-失败，1-成功
     */
    @Column(nullable = false)
    private int state;

    /**
     * 通知内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "datetime")
    private Date createTime;
}
