package com.monitor.web.alarm.dao;

import com.monitor.web.alarm.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberDAO extends JpaRepository<SubscriberEntity, Long> {
}
