package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ProjectUserRelationshipIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAssignUserToProject() throws Exception {
        User user = new User();
        user.setUsername("relationshipuser");
        User savedUser = userRepository.save(user);

        Project project = new Project();
        project.setName("Relationship Project");
        Project savedProject = projectRepository.save(project);

        mockMvc.perform(post("/projects/" + savedProject.getId() + "/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Project updatedProject = projectRepository.findById(savedProject.getId()).get();
        assertTrue(updatedProject.getUsers().stream()
                .anyMatch(u -> u.getId().equals(savedUser.getId())));
    }

    @Test
    public void testRemoveUserFromProject() throws Exception {
        User user = new User();
        user.setUsername("removeuser");
        User savedUser = userRepository.save(user);

        Project project = new Project();
        project.setName("Remove Project");
        project.getUsers().add(savedUser);
        Project savedProject = projectRepository.save(project);

        mockMvc.perform(delete("/projects/" + savedProject.getId() + "/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Project updatedProject = projectRepository.findById(savedProject.getId()).get();
        assertFalse(updatedProject.getUsers().stream()
                .anyMatch(u -> u.getId().equals(savedUser.getId())));
    }
}