package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.ResourceLoadErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ResourceLoadErrorLogDao extends JpaRepository<ResourceLoadErrorLog, Long> {

    @Query(value = "select count(id) from lms_resource_load_error_log t where t.create_time between ?1 and ?2", nativeQuery = true)
    int getCountByIdBetweenStartTimeAndEndTime(Date startTime, Date endTime);

    @Query(value = "select date_format(create_time, '%Y-%m-%d %H') as hour, count(id) as count from lms_resource_load_error_log " +
            "where project_identifier=?3 and create_time between ?1 and ?2 group by hour", nativeQuery = true)
    List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime, String projectIdentifier);
}

