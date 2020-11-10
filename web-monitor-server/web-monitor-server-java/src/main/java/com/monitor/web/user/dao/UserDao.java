package com.monitor.web.user.dao;

import com.monitor.web.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUsernameAndPassword(String username, String password);
}
