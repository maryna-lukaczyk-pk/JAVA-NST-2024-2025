package org.jerzy.projectmanagerapp.service;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;

  public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  public List<Project> getAllProjects() {
    return this.projectRepository.findAll();
  }

  public Project getById(String id) {
    int projectId = 0;
    try {
      projectId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid id: " + e);
    }
    return this.projectRepository.findById(projectId).get();
  }

  public Project create(Project project) {
    return this.projectRepository.save(project);
  }

  public Project update(String id, Project project) {
    int projectId;
    try {
      projectId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid project ID: " + id);
    }

    return this.projectRepository.findById(projectId).map(existingProject -> {
      existingProject.setName(project.getName());
      existingProject.setUsers(project.getUsers());
      existingProject.setTasks(project.getTasks());
      return this.projectRepository.save(existingProject);
    }).orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
  }

  public void delete(String id) {
    int projectId;
    try {
      projectId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid project ID: " + id);
    }

    if (!this.projectRepository.existsById(projectId)) {
      throw new IllegalArgumentException("Project not found with ID: " + id);
    }

    this.projectRepository.deleteById(projectId);
  }
}
