package com.monitor.web.alarm.dao;

import com.monitor.web.alarm.entity.AlarmSchedulerRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmSchedulerRelationDAO extends JpaRepository<AlarmSchedulerRelationEntity, Long> {
}
