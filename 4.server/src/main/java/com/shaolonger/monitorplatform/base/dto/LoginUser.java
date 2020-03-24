package com.shaolonger.monitorplatform.base.dto;

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
     * token
     */
    private String token;
}
