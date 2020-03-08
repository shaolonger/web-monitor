package com.shaolonger.monitorplatform.user.dao;

import com.shaolonger.monitorplatform.user.entity.UserProjectRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRelationDao extends JpaRepository<UserProjectRelationEntity, Long> {
}
