package com.monitor.web.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像
     */
    private String icon;

    /**
     * 性别
     * 0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 是否超级管理员
     * 0-否，1-是
     */
    private Integer isAdmin;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
