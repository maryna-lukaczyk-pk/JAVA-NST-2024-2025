package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.entity.ProjectUsers;
import com.example.demo.entity.Users;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.ProjectUsersRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectUsersService {

    @Autowired
    private ProjectUsersRepository projectUsersRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProjectUsers> getUsersByProject(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.map(projectUsersRepository::findByProject).orElse(null);
    }

    public List<ProjectUsers> getProjectsByUser(Long userId) {
        Optional<Users> user = userRepository.findById(userId);
        return user.map(projectUsersRepository::findByUser).orElse(null);
    }

    public ProjectUsers assignUserToProject(Long projectId, Long userId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Optional<Users> userOptional = userRepository.findById(userId);

        if (projectOptional.isPresent() && userOptional.isPresent()) {
            Project project = projectOptional.get();
            Users user = userOptional.get();

            // Check if the relationship already exists
            Optional<ProjectUsers> existingRelation = projectUsersRepository.findByProjectAndUser(project, user);
            if (existingRelation.isPresent()) {
                return existingRelation.get();
            }

            // Create new relationship
            ProjectUsers projectUsers = new ProjectUsers();
            projectUsers.setProject(project);
            projectUsers.setUser(user);
            return projectUsersRepository.save(projectUsers);
        }
        return null;
    }

    public void removeUserFromProject(Long projectId, Long userId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Optional<Users> userOptional = userRepository.findById(userId);

        if (projectOptional.isPresent() && userOptional.isPresent()) {
            Project project = projectOptional.get();
            Users user = userOptional.get();

            Optional<ProjectUsers> existingRelation = projectUsersRepository.findByProjectAndUser(project, user);
            existingRelation.ifPresent(projectUsersRepository::delete);
        }
    }
}
