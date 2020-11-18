package com.monitor.web.alarm.service;

import com.monitor.web.alarm.dao.AlarmSchedulerRelationDAO;
import com.monitor.web.alarm.dto.AlarmSchedulerRelationDTO;
import com.monitor.web.alarm.entity.AlarmSchedulerRelationEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AlarmSchedulerRelationService {

    @Autowired
    AlarmSchedulerRelationDAO alarmSchedulerRelationDAO;

    public boolean add(AlarmSchedulerRelationDTO alarmSchedulerRelationDTO) {

        // 从DTO中复制属性
        AlarmSchedulerRelationEntity alarmSchedulerRelationEntity = new AlarmSchedulerRelationEntity();
        BeanUtils.copyProperties(alarmSchedulerRelationDTO, alarmSchedulerRelationEntity);

        // 创建时间
        Date nowTime = new Date();
        alarmSchedulerRelationEntity.setCreateTime(nowTime);

        alarmSchedulerRelationDAO.save(alarmSchedulerRelationEntity);

        return true;
    }
}
