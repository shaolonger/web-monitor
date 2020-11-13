package com.monitor.web.alarm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ams_alarm")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 预警名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 项目标识
     */
    @Column(nullable = false)
    private String projectIdentifier;

    /**
     * 报警等级，-1-P4低，0-P3中，1-P2高，2-P1紧急
     */
    @Column(nullable = false)
    private int level;

    /**
     * 过滤条件，可多选。0-全部，1-JS_ERROR，2-HTTP_ERROR，3-RESOURCE_LOAD，4-CUSTOM_ERROR
     */
    @Column(nullable = false)
    private int category;

    /**
     * 预警规则，存放JSON格式
     */
    @Column(nullable = false)
    private String rule;

    /**
     * 报警时段-开始时间，例如00:00:00
     */
    @Column(nullable = false)
    private String startTime;

    /**
     * 报警时段-结束时间，例如23:59:59
     */
    @Column(nullable = false)
    private String endTime;

    /**
     * 静默期，0-不静默，1-5分钟，2-10分钟，3-15分钟，4-30分钟，5-1小时，6-3小时，7-12小时，8-24小时，9-当天
     */
    @Column(nullable = false)
    private int silentPeriod;

    /**
     * 是否启用，0-否，1-是
     */
    @Column(nullable = false)
    private int isActive;

    /**
     * 创建时间，格式yyyy-MM-dd HH:mm:ss
     */
    private Date createTime;

    /**
     * 更新时间，格式yyyy-MM-dd HH:mm:ss
     */
    private Date updateTime;

    /**
     * 创建人ID
     */
    @Column(nullable = false)
    private Long createBy;

    /**
     * 是否已被删除，0-否，1-是
     */
    @Column(nullable = false)
    private int isDeleted;
}
