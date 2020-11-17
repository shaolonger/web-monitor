package com.monitor.web.schedule.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SchedulerRecordDTO {

    /**
     * 定时任务id
     */
    @NotNull(message = "schedulerId不能为空")
    private Long schedulerId;

    /**
     * 执行状态，0-失败，1-成功
     */
    @NotEmpty(message = "state不能为空")
    private Integer state;

    /**
     * 定时任务执行的时长，单位毫秒
     */
    private Integer timeCost;

    /**
     * 执行失败的异常信息
     */
    private String errorMsg;
}
