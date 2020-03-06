package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.ResourceLoadErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceLoadErrorLogDao extends JpaRepository<ResourceLoadErrorLog, Long> {
}

