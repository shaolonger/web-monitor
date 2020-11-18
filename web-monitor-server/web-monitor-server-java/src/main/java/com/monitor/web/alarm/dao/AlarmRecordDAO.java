package com.monitor.web.alarm.dao;

import com.monitor.web.alarm.entity.AlarmRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRecordDAO extends JpaRepository<AlarmRecordEntity, Long> {

    List<AlarmRecordEntity> findAllByAlarmId(long alarmId);
}
