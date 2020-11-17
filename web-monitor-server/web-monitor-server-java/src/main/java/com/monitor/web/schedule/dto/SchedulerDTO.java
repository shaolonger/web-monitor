package com.monitor.web.schedule.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SchedulerDTO {

    /**
     * bean名称
     */
    @NotEmpty(message = "beanName不能为空")
    private String beanName;

    /**
     * bean中执行的方法名称
     */
    @NotEmpty(message = "methodName不能为空")
    private String methodName;

    /**
     * 方法的参数内容，JSON格式
     */
    private String params;

    /**
     * cron表达式
     */
    @NotEmpty(message = "cronExpression不能为空")
    private String cronExpression;

    /**
     * 执行状态，0-暂停，1-运行中
     */
    @NotNull(message = "state不能为空")
    private Integer state;
}
