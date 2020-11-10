package com.monitor.web.user.dao;

import com.monitor.web.user.entity.UserRegisterRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRegisterRecordDAO extends JpaRepository<UserRegisterRecordEntity, Long> {
    Optional<UserRegisterRecordEntity> findById(Long id);
}
