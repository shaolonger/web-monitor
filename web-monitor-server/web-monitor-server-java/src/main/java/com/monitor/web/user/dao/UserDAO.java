package com.monitor.web.user.dao;

import com.monitor.web.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByUsernameAndPassword(String username, String password);

    Optional<UserEntity> findById(Long id);
}
