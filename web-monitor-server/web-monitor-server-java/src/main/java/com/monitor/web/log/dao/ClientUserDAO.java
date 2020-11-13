package com.monitor.web.log.dao;

import com.monitor.web.log.entity.ClientUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientUserDAO extends JpaRepository<ClientUserEntity, Long> {

    @Query(value = "select count(distinct c_uuid) from lms_client_user", nativeQuery = true)
    int countDistinctCUuid();
}
