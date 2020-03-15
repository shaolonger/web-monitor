package com.shaolonger.monitorplatform.project.vo;

import lombok.Data;

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
