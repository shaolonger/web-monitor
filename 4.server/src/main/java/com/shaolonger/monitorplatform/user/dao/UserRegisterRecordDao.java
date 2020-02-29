package com.shaolonger.monitorplatform.user.dao;

import com.shaolonger.monitorplatform.user.entity.UserRegisterRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRegisterRecordDao extends JpaRepository<UserRegisterRecordEntity, Long> {
}
