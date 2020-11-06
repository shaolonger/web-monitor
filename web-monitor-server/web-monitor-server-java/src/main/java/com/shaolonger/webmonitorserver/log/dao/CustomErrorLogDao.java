package com.shaolonger.webmonitorserver.log.dao;

import com.shaolonger.webmonitorserver.log.entity.CustomErrorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomErrorLogDao extends JpaRepository<CustomErrorLogEntity, Long> {

    @Query(value = "select count(id) from lms_custom_error_log where project_identifier=?1 and create_time between ?2 and ?3", nativeQuery = true)
    int getCountByIdBetweenStartTimeAndEndTime(String projectIdentifier, Date startTime, Date endTime);

    @Query(value = "select date_format(create_time, '%Y-%m-%d %H') as hour, count(id) as count from lms_custom_error_log " +
            "where project_identifier=?3 and create_time between ?1 and ?2 group by hour", nativeQuery = true)
    List<Map<String, Object>> getLogCountByHours(Date startTime, Date endTime, String projectIdentifier);

    @Query(value = "select date_format(create_time, '%Y-%m-%d') as day, count(id) as count from lms_custom_error_log " +
            "where project_identifier=?3 and create_time between ?1 and date_add(?2, interval 1 day) group by day", nativeQuery = true)
    List<Map<String, Object>> getLogCountByDays(Date startTime, Date endTime, String projectIdentifier);

    @Query(value = "select id, user_id, create_time from lms_custom_error_log " +
            "where project_identifier=?1 and create_time between ?2 and ?3", nativeQuery = true)
    List<Map<String, Object>> getLogListByCreateTimeAndProjectIdentifier(String projectIdentifier, Date startTime, Date endTime);

    @Query(value = "select * from lms_custom_error_log where project_identifier=?1 and create_time between ?2 and ?3", nativeQuery = true)
    List<Map<String, Object>> findAllByProjectIdentifierAndCreateTimeBetween(String projectIdentifier, Date startTime, Date endTime);
}
