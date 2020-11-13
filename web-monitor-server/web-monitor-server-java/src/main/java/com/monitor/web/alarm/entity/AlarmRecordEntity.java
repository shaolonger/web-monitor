package com.monitor.web.alarm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ams_alarm_record")
public class AlarmRecordEntity {

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
     * 报警内容，格式为JSON字符串
     */
    @Column(nullable = false)
    private String alarmData;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private Date createTime;
}
