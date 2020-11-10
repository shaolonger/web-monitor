package com.monitor.web.alarm.dao;

import com.monitor.web.alarm.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmDao extends JpaRepository<AlarmEntity, Long> {

    Optional<AlarmEntity> getById(Long id);
}
