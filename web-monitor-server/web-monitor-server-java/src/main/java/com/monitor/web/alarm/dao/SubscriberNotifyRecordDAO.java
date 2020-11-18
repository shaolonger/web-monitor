package com.monitor.web.alarm.dao;

import com.monitor.web.alarm.entity.SubscriberNotifyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberNotifyRecordDAO extends JpaRepository<SubscriberNotifyRecordEntity, Long> {

    List<SubscriberNotifyRecordEntity> findAllByAlarmRecordId(long alarmRecordId);
}
