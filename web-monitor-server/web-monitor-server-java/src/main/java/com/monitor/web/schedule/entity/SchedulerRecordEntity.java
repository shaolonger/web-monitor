package com.monitor.web.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tms_scheduler_record")
public class SchedulerRecordEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 定时任务id
     */
    @Column(nullable = false)
    private Long schedulerId;

    /**
     * 执行状态，0-失败，1-成功
     */
    @Column(nullable = false)
    private Integer state;

    /**
     * 定时任务执行的时长，单位毫秒
     */
    private Integer timeCost;

    /**
     * 执行失败的异常信息
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private Date createTime;
}
