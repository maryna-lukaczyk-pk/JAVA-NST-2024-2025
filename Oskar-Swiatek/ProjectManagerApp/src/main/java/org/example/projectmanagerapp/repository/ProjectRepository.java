package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
