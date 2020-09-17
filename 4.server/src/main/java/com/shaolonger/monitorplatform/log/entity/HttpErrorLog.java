package com.shaolonger.monitorplatform.log.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "lms_http_error_log")
@Data
public class HttpErrorLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
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
     * Http类型
     * 如"request"、"response"
     */
    private String httpType;

    /**
     * 完整的Http地址
     */
    @NotEmpty(message = "httpUrlComplete不能为空")
    @Column(columnDefinition = "TEXT")
    private String httpUrlComplete;

    /**
     * 缩写的Http地址
     */
    @Column(columnDefinition = "TEXT")
    private String httpUrlShort;

    /**
     * Http请求状态
     */
    @NotEmpty(message = "status不能为空")
    private String status;

    /**
     * Http请求状态文字描述
     */
    private String statusText;

    /**
     * 响应时长
     */
    private String resTime;
}
