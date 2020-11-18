package com.monitor.web.schedule.service;

import com.monitor.web.schedule.component.CronTaskRegistrar;
import com.monitor.web.schedule.component.SchedulingRunnable;
import com.monitor.web.schedule.dao.SchedulerDAO;
import com.monitor.web.schedule.dto.SchedulerDTO;
import com.monitor.web.schedule.entity.SchedulerEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SchedulerService {

    @Autowired
    SchedulerDAO schedulerDAO;
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    /**
     * 新增
     *
     * @param schedulerDTO schedulerDTO
     * @return Long
     */
    public SchedulerEntity add(SchedulerDTO schedulerDTO) {

        // 从DTO中复制属性
        SchedulerEntity schedulerEntity = new SchedulerEntity();
        BeanUtils.copyProperties(schedulerDTO, schedulerEntity);

        // 创建时间
        Date nowTime = new Date();
        schedulerEntity.setCreateTime(nowTime);

        return schedulerDAO.save(schedulerEntity);
    }

    /**
     * 根据state获取数据集
     *
     * @param state state
     * @return List
     */
    public List<SchedulerEntity> getListByState(int state) {
        List<SchedulerEntity> resultList = new ArrayList<>();
        if (state < 0 || state > 1) {
            return resultList;
        }
        return schedulerDAO.getAllByState(state);
    }

    /**
     * 启动定时任务
     *
     * @param schedulerEntity schedulerEntity
     */
    public void startScheduler(SchedulerEntity schedulerEntity) {

        String beanName = schedulerEntity.getBeanName();
        String methodName = schedulerEntity.getMethodName();
        String params = schedulerEntity.getParams();
        String cronExpression = schedulerEntity.getCronExpression();
        Long schedulerId = schedulerEntity.getId();

        SchedulingRunnable task = new SchedulingRunnable(beanName, methodName, params, schedulerId);
        cronTaskRegistrar.addCronTask(task, cronExpression);
    }

    /**
     * 停止定时任务
     *
     * @param schedulerEntity schedulerEntity
     */
    public void stopScheduler(SchedulerEntity schedulerEntity) {

        String beanName = schedulerEntity.getBeanName();
        String methodName = schedulerEntity.getMethodName();
        String params = schedulerEntity.getParams();
        Long schedulerId = schedulerEntity.getId();

        SchedulingRunnable task = new SchedulingRunnable(beanName, methodName, params, schedulerId);
        cronTaskRegistrar.removeCronTask(task);
    }

    /**
     * 查询
     *
     * @param id id
     * @return Optional
     */
    public Optional<SchedulerEntity> getById(long id) {
        return schedulerDAO.findById(id);
    }

    /**
     * 删除
     *
     * @param schedulerEntity schedulerEntity
     */
    public void deleteByEntity(SchedulerEntity schedulerEntity) {
        schedulerDAO.delete(schedulerEntity);
    }
}
