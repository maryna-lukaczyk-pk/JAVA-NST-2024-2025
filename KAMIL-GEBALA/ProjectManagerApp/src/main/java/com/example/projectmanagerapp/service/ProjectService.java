package com.example.projectmanagerapp.service;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.ProjectUser;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.ProjectUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(long id,Project project) {
        if(projectRepository.existsById(id)){
            return projectRepository.save(project);
        }
        return null;
    }

    public void deleteProject(long id) {projectRepository.deleteById(id); }

    public ProjectUser assignUserToProject(Long projectId, Long userId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        Optional<Users> userOpt = usersService.getUserById(userId);

        if (projectOpt.isPresent() && userOpt.isPresent()) {
            Project project = projectOpt.get();
            Users user = userOpt.get();

            ProjectUser existingRelation = projectUserRepository.findByProjectAndUser(project, user);
            if (existingRelation != null) {
                return existingRelation;
            }

            ProjectUser projectUser = createProjectUser(project, user);

            return projectUserRepository.save(projectUser);
        }

        return null;
    }

    public List<Users> getUsersInProject(Long projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            List<ProjectUser> projectUsers = projectUserRepository.findByProject(projectOpt.get());
            return projectUsers.stream()
                    .map(pu -> pu.getUser())
                    .toList();
        }

        return List.of();
    }

    private ProjectUser createProjectUser(Project project, Users user) {
        ProjectUser projectUser = new ProjectUser();
        try {
            java.lang.reflect.Field projectField = ProjectUser.class.getDeclaredField("project");
            projectField.setAccessible(true);
            projectField.set(projectUser, project);

            java.lang.reflect.Field userField = ProjectUser.class.getDeclaredField("user");
            userField.setAccessible(true);
            userField.set(projectUser, user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating ProjectUser: " + e.getMessage(), e);
        }
        return projectUser;
    }
}
