package com.monitor.web.schedule.dao;

import com.monitor.web.schedule.entity.SchedulerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerDAO extends JpaRepository<SchedulerEntity, Long> {
}
