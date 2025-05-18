package org.jerzy.projectmanagerapp.service;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.jerzy.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
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
    return this.projectRepository.findById((long) projectId).get();
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

    return this.projectRepository.findById((long) projectId).map(existingProject -> {
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

    if (!this.projectRepository.existsById((long) projectId)) {
      throw new IllegalArgumentException("Project not found with ID: " + id);
    }

    this.projectRepository.deleteById((long) projectId);
  }

  public void assignUserToProject(Long projectId, Long userId) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    project.getUsers().add(user);
    projectRepository.save(project);
  }

}
