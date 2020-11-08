package com.shaolonger.webmonitorserver.alarm.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "ams_alarm")
public class AlarmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 预警名称
     */
    @NotEmpty(message = "name不能为空")
    private String name;

    /**
     * 项目标识
     */
    @NotEmpty(message = "projectIdentifier不能为空")
    private String projectIdentifier;

    /**
     * 报警等级，-1-P4低，0-P3中，1-P2高，2-P1紧急
     */
    @NotNull(message = "level不能为空")
    private int level;

    /**
     * 过滤条件，可多选。0-全部，1-JS_ERROR，2-HTTP_ERROR，3-RESOURCE_LOAD，4-CUSTOM_ERROR
     */
    @NotNull(message = "category不能为空")
    private int category;

    /**
     * 预警规则，存放JSON格式
     */
    @NotEmpty(message = "rule不能为空")
    private String rule;

    /**
     * 报警时段-开始时间，例如00:00:00
     */
    @NotNull(message = "startTime不能为空")
    private String startTime;

    /**
     * 报警时段-结束时间，例如23:59:59
     */
    @NotNull(message = "endTime不能为空")
    private String endTime;

    /**
     * 静默期，0-不静默，1-5分钟，2-10分钟，3-15分钟，4-30分钟，5-1小时，6-3小时，7-12小时，8-24小时，9-当天
     */
    @NotNull(message = "silentPeriod不能为空")
    private int silentPeriod;

    /**
     * 是否启用，0-否，1-是
     */
    @NotNull(message = "isActive不能为空")
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
    @NotNull(message = "createBy不能为空")
    private Long createBy;

    /**
     * 是否已被删除，0-否，1-是
     */
    @NotNull(message = "isDeleted")
    private int isDeleted;
}
