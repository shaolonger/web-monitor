package com.monitor.web.schedule.service;

import com.monitor.web.schedule.dao.SchedulerDAO;
import com.monitor.web.schedule.dto.SchedulerDTO;
import com.monitor.web.schedule.entity.SchedulerEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SchedulerService {

    @Autowired
    SchedulerDAO schedulerDAO;

    /**
     * 新增
     *
     * @param schedulerDTO schedulerDTO
     * @return Long
     */
    public Long add(SchedulerDTO schedulerDTO) {

        // 从DTO中复制属性
        SchedulerEntity schedulerEntity = new SchedulerEntity();
        BeanUtils.copyProperties(schedulerDTO, schedulerEntity);

        // 创建时间
        Date nowTime = new Date();
        schedulerEntity.setCreateTime(nowTime);

        schedulerDAO.save(schedulerEntity);
        return schedulerEntity.getId();
    }
}
