package com.shaolonger.webmonitorserver.user.dao;

import com.shaolonger.webmonitorserver.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUsernameAndPassword(String username, String password);
}
