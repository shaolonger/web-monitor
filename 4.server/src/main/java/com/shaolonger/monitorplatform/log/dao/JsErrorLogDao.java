package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.JsErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JsErrorLogDao extends JpaRepository<JsErrorLog, Long> {
}
