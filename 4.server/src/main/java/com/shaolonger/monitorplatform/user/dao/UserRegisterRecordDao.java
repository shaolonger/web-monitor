package com.shaolonger.monitorplatform.user.dao;

import com.shaolonger.monitorplatform.user.entity.UserRegisterRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRegisterRecordDao extends JpaRepository<UserRegisterRecordEntity, Long> {
    Optional<UserRegisterRecordEntity> findById(Long id);
}
