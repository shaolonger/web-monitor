package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.CustomErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomErrorLogDao extends JpaRepository<CustomErrorLog, Long> {

    @Query(value = "select count(id) from lms_custom_error_log where create_time between ?1 and ?2", nativeQuery = true)
    int getCountByIdBetweenStartTimeAndEndTime(Date startTime, Date endTime);

    @Query(value = "select date_format(create_time, '%Y-%m-%d %H') as hour, count(id) as count from lms_custom_error_log " +
            "where create_time between ?1 and ?2 group by hour", nativeQuery = true)
    List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime);
}
