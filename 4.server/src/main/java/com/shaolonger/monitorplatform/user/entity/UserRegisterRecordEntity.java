package com.shaolonger.monitorplatform.user.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ums_user_register_record")
@Data
public class UserRegisterRecordEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /**
     * 用户名
     */
    @NotEmpty(message = "username不能为空")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "password不能为空")
    private String password;

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
    @NotEmpty(message = "email不能为空")
    private String email;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 审批人ID
     */
    private Long auditUser;

    /**
     * 审批结果
     * 0-不通过，1-通过
     */
    private Integer auditResult;
}
