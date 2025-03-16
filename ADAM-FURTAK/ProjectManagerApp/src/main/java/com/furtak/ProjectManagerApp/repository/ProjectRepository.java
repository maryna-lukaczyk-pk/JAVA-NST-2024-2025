package com.furtak.ProjectManagerApp.repository;

import com.furtak.ProjectManagerApp.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {

}