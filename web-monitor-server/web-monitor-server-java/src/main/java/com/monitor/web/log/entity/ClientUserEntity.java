package com.monitor.web.log.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "lms_client_user")
public class ClientUserEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 客户端唯一标识码
     */
    @JsonProperty(value = "cUuid")
    @Column(nullable = false)
    private String cUuid;

    /**
     * 业务用户ID
     */
    @JsonProperty(value = "bUid")
    @Column(nullable = false)
    private Long bUid;

    /**
     * 业务用户名
     */
    @JsonProperty(value = "bUname")
    @Column(nullable = false)
    private String bUname;

    /**
     * 创建时间
     */
    @Column(nullable = false, columnDefinition = "datetime")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(nullable = false, columnDefinition = "datetime")
    private Date updateTime;
}
