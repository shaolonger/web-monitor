package com.shaolonger.webmonitorserver.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于token保存的用户信息
 */
@Getter
@Setter
public class LoginUser {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 是否超级管理员
     */
    private Integer isAdmin;

    /**
     * token
     */
    private String token;
}
