package com.shaolonger.monitorplatform.log.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lms_js_error_log")
@Data
public class JsErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

    private String logType;

    private Date createTime;

    private Long userId;

    private String userName;

    private String pageUrl;

    private String pageKey;

    private String deviceName;

    private String os;

    private String browserName;

    private String browserVersion;

    private String ipAddress;

    private String errorType;
    
    private String errorMessage;

    private String errorStack;
}
