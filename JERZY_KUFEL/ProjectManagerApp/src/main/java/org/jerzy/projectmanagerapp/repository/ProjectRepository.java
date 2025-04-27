package org.jerzy.projectmanagerapp.repository;

import org.jerzy.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}