package com.monitor.web.schedule.service;

import com.monitor.web.schedule.entity.SchedulerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerRunner implements CommandLineRunner {

    @Autowired
    SchedulerService schedulerService;

    @Override
    public void run(String... args) {

        // 获取所有运行中的定时任务
        int state = 1;
        List<SchedulerEntity> list = schedulerService.getListByState(state);

        // 启动任务
        if (list.size() > 0) {
            list.forEach(schedulerService::startScheduler);
        }
    }
}
