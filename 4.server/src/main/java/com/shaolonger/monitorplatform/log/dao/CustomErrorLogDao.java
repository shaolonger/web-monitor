package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.CustomErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomErrorLogDao extends JpaRepository<CustomErrorLog, Long> {
}
