
package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.repository.ProjectUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;

    public ProjectUserService(ProjectUserRepository projectUserRepository) {
        this.projectUserRepository = projectUserRepository;
    }

    public List<ProjectUser> getAllProjectUsers() {
        return projectUserRepository.findAll();
    }

    public ProjectUser createProjectUser(ProjectUser projectUser) {
        return projectUserRepository.save(projectUser);
    }
}
