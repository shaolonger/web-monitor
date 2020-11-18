package com.monitor.web.alarm.dao;

import com.monitor.web.alarm.entity.AlarmSchedulerRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmSchedulerRelationDAO extends JpaRepository<AlarmSchedulerRelationEntity, Long> {

    List<AlarmSchedulerRelationEntity> findAllByAlarmId(long alarmId);
}
