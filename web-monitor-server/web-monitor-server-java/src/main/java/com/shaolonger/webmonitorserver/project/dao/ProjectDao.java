package com.shaolonger.webmonitorserver.project.dao;

import com.shaolonger.webmonitorserver.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectDao extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findById(Long id);

    Optional<ProjectEntity> findByProjectIdentifier(String projectIdentifier);
}
