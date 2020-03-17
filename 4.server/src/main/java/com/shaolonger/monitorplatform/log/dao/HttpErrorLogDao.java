package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.HttpErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface HttpErrorLogDao extends JpaRepository<HttpErrorLog, Long> {

    @Query(value = "select count(id) from lms_http_error_log t where t.create_time between ?1 and ?2", nativeQuery = true)
    int getCountByIdBetweenStartTimeAndEndTime(Date startTime, Date endTime);
}

