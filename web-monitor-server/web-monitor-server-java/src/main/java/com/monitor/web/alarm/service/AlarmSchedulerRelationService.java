package com.monitor.web.alarm.service;

import com.monitor.web.alarm.dao.AlarmSchedulerRelationDAO;
import com.monitor.web.alarm.dto.AlarmSchedulerRelationDTO;
import com.monitor.web.alarm.entity.AlarmSchedulerRelationEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    /**
     * 通过alarmId查找实体
     *
     * @param alarmId alarmId
     * @return SchedulerEntity
     */
    public List<AlarmSchedulerRelationEntity> findAllByAlarmId(long alarmId) {
        return alarmSchedulerRelationDAO.findAllByAlarmId(alarmId);
    }

    /**
     * 删除
     *
     * @param entity entity
     */
    public void deleteByEntity(AlarmSchedulerRelationEntity entity) {
        alarmSchedulerRelationDAO.delete(entity);
    }
}
