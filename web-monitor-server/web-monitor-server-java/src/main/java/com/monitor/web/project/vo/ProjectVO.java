package com.monitor.web.project.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProjectVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目标识
     */
    private String projectIdentifier;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 接入方式
     */
    @NotEmpty(message = "accessType不能为空")
    private String accessType;

    /**
     * 开启功能
     */
    private String activeFuncs;

    /**
     * 是否自动上报
     */
    @NotNull(message = "isAutoUpload不能为空")
    private Integer isAutoUpload;

    /**
     * 预警模块中的钉钉机器人access_token，用于预警模块中发送报警推送，多个用英文逗号隔开
     */
    private String notifyDtToken;

    /**
     * 预警模块中的邮件推送地址，多个用英文逗号隔开
     */
    private String notifyEmail;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 项目关联用户
     */
    private String userList;
}
