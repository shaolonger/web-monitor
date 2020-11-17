package com.monitor.web.schedule.dao;

import com.monitor.web.schedule.entity.SchedulerRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRecordDAO extends JpaRepository<SchedulerRecordEntity, Long> {
}
