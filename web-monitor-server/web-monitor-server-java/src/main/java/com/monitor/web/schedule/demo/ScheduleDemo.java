package com.monitor.web.schedule.demo;

import org.springframework.stereotype.Component;

@Component("scheduleDemo")
public class ScheduleDemo {

    public void taskWithParams(String params) {
        System.out.println("执行有参示例任务：" + params);
    }

    public void taskWithNoParams() {
        System.out.println("执行无参示例任务");
    }
}
