package com.monitor.web.log.dao;

import com.monitor.web.log.entity.ClientUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientUserDAO extends JpaRepository<ClientUserEntity, Long> {
}
