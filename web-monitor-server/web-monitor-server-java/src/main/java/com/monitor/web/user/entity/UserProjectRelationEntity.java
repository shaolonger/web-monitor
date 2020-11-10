package com.monitor.web.user.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ums_user_project_relation")
@Data
public class UserProjectRelationEntity {
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
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 密码
     */
    @NotNull(message = "projectId不能为空")
    private Long projectId;
}
