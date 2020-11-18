package com.monitor.web.schedule.component;

import com.monitor.web.schedule.dto.SchedulerRecordDTO;
import com.monitor.web.schedule.service.SchedulerRecordService;
import com.monitor.web.utils.BeanContextUtils;
import com.monitor.web.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class SchedulingRunnable implements Runnable {

    private String beanName = null;

    private String methodName = null;

    private String params = null;

    private Long schedulerId = null;

    public SchedulingRunnable(String beanName, String methodName, Long schedulerId) {
        this(beanName, methodName, null, schedulerId);
    }

    public SchedulingRunnable(String beanName, String methodName, String params, Long schedulerId) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        this.schedulerId = schedulerId;
    }

    @Override
    public void run() {
        log.info("定时任务开始执行 - bean：{}，方法：{}，参数：{}", beanName, methodName, params);
        int state = 0;
        String errorMsg = "";
        long startTime = System.currentTimeMillis();
        try {
            Object target = SpringContextUtils.getBean(beanName);
            Method method = null;
            if (params != null) {
                method = target.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }
            ReflectionUtils.makeAccessible(method);
            if (!StringUtils.isEmpty(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
            state = 1;
        } catch (Exception e) {
            log.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), e);
            errorMsg = e.getMessage();
        }
        int timeCost = (int) (System.currentTimeMillis() - startTime);
        log.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", beanName, methodName, params, timeCost);

        // 保存定时任务执行记录
        SchedulerRecordDTO schedulerRecordDTO = new SchedulerRecordDTO();
        schedulerRecordDTO.setSchedulerId(this.schedulerId);
        schedulerRecordDTO.setState(state);
        schedulerRecordDTO.setErrorMsg(errorMsg);
        schedulerRecordDTO.setTimeCost(timeCost);
        SchedulerRecordService schedulerRecordService = BeanContextUtils.getBean(SchedulerRecordService.class);
        schedulerRecordService.add(schedulerRecordDTO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return beanName.equals(that.beanName) &&
                    methodName.equals(that.methodName) &&
                    schedulerId.equals(that.schedulerId) &&
                    that.params == null;
        }
        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                schedulerId.equals(that.schedulerId) &&
                params.equals(that.params);
    }

    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName, schedulerId);
        }
        return Objects.hash(beanName, methodName, params, schedulerId);
    }
}
