package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.HttpErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpErrorLogDao extends JpaRepository<HttpErrorLog, Long> {
}

