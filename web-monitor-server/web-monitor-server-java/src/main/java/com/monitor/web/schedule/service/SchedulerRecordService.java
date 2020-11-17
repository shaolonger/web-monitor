package com.monitor.web.schedule.service;

import com.monitor.web.schedule.dao.SchedulerRecordDAO;
import com.monitor.web.schedule.dto.SchedulerRecordDTO;
import com.monitor.web.schedule.entity.SchedulerRecordEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SchedulerRecordService {

    @Autowired
    SchedulerRecordDAO schedulerRecordDAO;

    /**
     * 新增
     *
     * @param schedulerRecordDTO schedulerRecordDTO
     * @return boolean
     */
    public boolean add(SchedulerRecordDTO schedulerRecordDTO) {

        // 从DTO中复制属性
        SchedulerRecordEntity schedulerRecordEntity = new SchedulerRecordEntity();
        BeanUtils.copyProperties(schedulerRecordDTO, schedulerRecordEntity);

        // 创建时间
        Date nowTime = new Date();
        schedulerRecordEntity.setCreateTime(nowTime);

        schedulerRecordDAO.save(schedulerRecordEntity);
        return true;
    }
}
