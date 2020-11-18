package com.monitor.web.project.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "pms_project")
@Data
public class ProjectEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 项目名
     */
    @Column(nullable = false)
    private String projectName;

    /**
     * 项目标识
     */
    @Column(nullable = false)
    private String projectIdentifier;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 接入方式
     */
    @Column(nullable = false)
    private String accessType;

    /**
     * 开启功能
     */
    @Column(nullable = false)
    private String activeFuncs;

    /**
     * 是否自动上报
     */
    @Column(nullable = false)
    private Integer isAutoUpload;

    /**
     * 预警模块中的钉钉机器人access_token，用于预警模块中发送报警推送，多个用英文逗号隔开
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String notifyDtToken;

    /**
     * 预警模块中的邮件推送地址，多个用英文逗号隔开
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String notifyEmail;

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
