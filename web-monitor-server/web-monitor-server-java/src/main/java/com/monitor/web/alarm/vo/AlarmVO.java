package com.monitor.web.alarm.vo;

import lombok.Data;

@Data
public class AlarmVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 预警名称
     */
    private String name;

    /**
     * 项目标识
     */
    private String projectIdentifier;

    /**
     * 报警等级，-1-P4低，0-P3中，1-P2高，2-P1紧急
     */
    private int level;

    /**
     * 过滤条件，可多选。0-全部，1-JS_ERROR，2-HTTP_ERROR，3-RESOURCE_LOAD，4-CUSTOM_ERROR
     */
    private int category;

    /**
     * 预警规则，存放JSON格式
     */
    private String rule;

    /**
     * 报警时段-开始时间，例如00:00:00
     */
    private String startTime;

    /**
     * 报警时段-结束时间，例如23:59:59
     */
    private String endTime;

    /**
     * 静默期，0-不静默，1-5分钟，2-10分钟，3-15分钟，4-30分钟，5-1小时，6-3小时，7-12小时，8-24小时，9-当天
     */
    private int silentPeriod;

    /**
     * 是否启用，0-否，1-是
     */
    private int isActive;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 是否已被删除，0-否，1-是
     */
    private int isDeleted;

    /**
     * 通知方式，category为1-钉钉机器人，2-邮箱
     */
    private String subscriberList;
}
