package com.monitor.web.schedule.dao;

import com.monitor.web.schedule.entity.SchedulerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulerDAO extends JpaRepository<SchedulerEntity, Long> {

    List<SchedulerEntity> getAllByState(int state);
}
