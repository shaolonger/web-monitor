package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.ResourceLoadErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ResourceLoadErrorLogDao extends JpaRepository<ResourceLoadErrorLog, Long> {

    @Query(value = "select count(id) from lms_resource_load_error_log t where t.create_time between ?1 and ?2", nativeQuery = true)
    int countByIdBetweenStartTimeAndEndTime(Date startTime, Date endTime);
}

