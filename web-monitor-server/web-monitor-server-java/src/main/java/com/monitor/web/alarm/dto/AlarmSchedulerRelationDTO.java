package com.monitor.web.alarm.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AlarmSchedulerRelationDTO {

    /**
     * 预警id
     */
    @NotNull(message = "alarmId不能为空")
    private Long alarmId;

    /**
     * 定时任务id
     */
    @NotNull(message = "schedulerId不能为空")
    private Long schedulerId;
}
