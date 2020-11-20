package com.monitor.web.alarm.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "ams_subscriber")
public class SubscriberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 预警规则id
     */
    @Column(nullable = false)
    private Long alarmId;

    /**
     * 报警订阅方，内容为JSON格式，例如"[]"。
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String subscriber;

    /**
     * 是否启用，0-否，1-是
     */
    @Column(nullable = false)
    private int isActive;

    /**
     * 订阅类型，1-钉钉机器人，2-邮箱
     */
    @Column(nullable = false)
    private int category;
}
