package com.shaolonger.webmonitorserver.alarm.dao;

import com.shaolonger.webmonitorserver.alarm.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmDao extends JpaRepository<AlarmEntity, Long> {

}
