package com.monitor.web.alarm.dao;

import com.monitor.web.alarm.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberDAO extends JpaRepository<SubscriberEntity, Long> {

     void deleteAllByAlarmId(Long alarmId);

     List<SubscriberEntity> getAllByAlarmId(Long alarmId);
}
