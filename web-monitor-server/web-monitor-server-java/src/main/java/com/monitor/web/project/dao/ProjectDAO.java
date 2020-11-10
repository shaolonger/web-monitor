package com.monitor.web.project.dao;

import com.monitor.web.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectDAO extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findById(Long id);

    Optional<ProjectEntity> findByProjectIdentifier(String projectIdentifier);
}
