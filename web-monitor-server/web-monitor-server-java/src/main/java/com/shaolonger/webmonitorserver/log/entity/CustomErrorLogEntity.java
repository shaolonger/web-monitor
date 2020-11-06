package com.shaolonger.webmonitorserver.log.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "lms_custom_error_log")
@Data
public class CustomErrorLogEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 项目标识
     */
    @NotEmpty(message = "projectIdentifier不能为空")
    private String projectIdentifier;

    /**
     * 日志类型
     * JS_ERROR、HTTP_ERROR、RESOURCE_LOAD、CUSTOM_ERROR
     */
    @NotEmpty(message = "logType不能为空")
    private String logType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 客户端唯一标识码
     */
    @NotEmpty(message = "cUuid不能为空")
    private String cUuid;

    /**
     * 业务用户ID
     */
    private Long bUid;

    /**
     * 业务用户名
     */
    private String bUname;

    /**
     * 页面URL
     */
    @Column(columnDefinition = "TEXT")
    private String pageUrl;

    /**
     * 页面关键字
     */
    private String pageKey;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 操作系统版本
     */
    private String osVersion;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 地址
     */
    private String address;

    /**
     * 网络类型
     */
    private String netType;

    /**
     * 异常类型
     */
    @Column(columnDefinition = "TEXT")
    private String errorType;

    /**
     * 异常信息
     */
    @NotEmpty(message = "errorMessage不能为空")
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
