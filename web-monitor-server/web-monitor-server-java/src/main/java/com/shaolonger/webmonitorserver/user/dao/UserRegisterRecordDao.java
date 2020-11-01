package com.shaolonger.webmonitorserver.user.dao;

import com.shaolonger.webmonitorserver.user.entity.UserRegisterRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRegisterRecordDao extends JpaRepository<UserRegisterRecordEntity, Long> {
    Optional<UserRegisterRecordEntity> findById(Long id);
}
