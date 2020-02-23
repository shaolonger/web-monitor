package com.shaolonger.monitorplatform.log.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "lms_custom_error_log")
@Data
public class CustomErrorLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 项目ID
     */
    @NotNull(message = "projectId不能为空")
    private Long projectId;

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
     * 用户ID
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 页面URL
     */
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
     * 异常类型
     */
    @NotEmpty(message = "errorType不能为空")
    private String errorType;

    /**
     * 异常信息
     */
    @NotEmpty(message = "errorMessage不能为空")
    private String errorMessage;
}
