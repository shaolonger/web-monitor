package com.monitor.web.alarm.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SubscriberDTO {

    @NotEmpty(message = "subscriber不能为空")
    private String subscriber;

    @NotNull(message = "isActive不能为空")
    private int isActive;

    @NotNull(message = "category不能为空")
    private int category;
}
