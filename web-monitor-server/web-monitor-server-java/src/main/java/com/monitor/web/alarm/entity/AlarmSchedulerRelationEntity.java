package com.monitor.web.alarm.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ams_alarm_scheduler_relation")
public class AlarmSchedulerRelationEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 预警id
     */
    @Column(nullable = false)
    private Long alarmId;

    /**
     * 定时任务id
     */
    @Column(nullable = false)
    private Long schedulerId;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private Date createTime;
}
