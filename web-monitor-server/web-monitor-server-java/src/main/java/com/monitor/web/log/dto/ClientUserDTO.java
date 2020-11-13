package com.monitor.web.log.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ClientUserDTO {

    /**
     * 客户端唯一标识码
     */
    @NotEmpty(message = "cUuid不能为空")
    private String cUuid;

    /**
     * 业务用户ID
     */
    private Long bUid;

    /**
     * 业务用户名
     */
    private String bUname;
}
