package com.shaolonger.monitorplatform.project.dao;

import com.shaolonger.monitorplatform.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectDao extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findById(Long id);
}
