package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Pobiera wszystkie projekty.
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Tworzy nowy projekt.
     */
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    /**
     * Pobiera projekt po ID.
     */
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));
    }

    /**
     * Aktualizuje istniejący projekt.
     */
    public Project updateProject(Long id, Project updated) {
        Project existing = getProjectById(id);
        existing.setName(updated.getName());
        return projectRepository.save(existing);
    }

    /**
     * Usuwa projekt po ID.
     */
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    /**
     * Przypisuje użytkowników do projektu.
     */
    public Project assignUsers(Long projectId, List<Long> userIds) {
        Project project = getProjectById(projectId);
        List<User> users = userRepository.findAllById(userIds);
        project.getUsers().addAll(users);
        return projectRepository.save(project);
    }
}
