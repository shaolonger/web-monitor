package com.shaolonger.monitorplatform.log.dao;

import com.shaolonger.monitorplatform.log.entity.JsErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface JsErrorLogDao extends JpaRepository<JsErrorLog, Long> {
    @Query(value = "select l from JsErrorLog l where (l.logType=?2) and (l.createTime>=?3 and l.createTime<=?4) and (l.userName like ?5) and (l.pageUrl like ?6) and (l.errorType like ?7) and (l.errorMessage like ?8)")
    Page<JsErrorLog> findByQueries(Long projectId, String logType, Date startTime, Date endTime, String userName, String pageUrl, String errorType, String errorMessage, Pageable pageable);
}
