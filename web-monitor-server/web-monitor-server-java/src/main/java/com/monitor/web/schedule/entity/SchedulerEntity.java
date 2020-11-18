package com.monitor.web.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tms_scheduler")
public class SchedulerEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * bean名称
     */
    @Column(nullable = false)
    private String beanName;

    /**
     * bean中执行的方法名称
     */
    @Column(nullable = false)
    private String methodName;

    /**
     * 方法的参数内容，JSON格式
     */
    @Column(columnDefinition = "TEXT")
    private String params;

    /**
     * cron表达式
     */
    @Column(nullable = false)
    private String cronExpression;

    /**
     * 执行状态，0-暂停，1-运行中
     */
    private Integer state;

    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "datetime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(columnDefinition = "datetime")
    private Date updateTime;
}
