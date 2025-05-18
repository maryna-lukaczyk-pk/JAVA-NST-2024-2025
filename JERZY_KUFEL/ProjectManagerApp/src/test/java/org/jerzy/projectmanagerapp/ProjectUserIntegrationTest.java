package org.jerzy.projectmanagerapp;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.jerzy.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Transactional
// @ImportAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class ProjectUserIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void assignUserToProject_shouldSucceed() throws Exception {
        // (a) Utwórz użytkownika
        User user = new User();
        user.setUsername("Test User");
        user = userRepository.save(user);

        // (a) Utwórz projekt
        Project project = new Project();
        project.setName("Test Project");
        project = projectRepository.save(project);

        // (b) Wywołaj przypisanie użytkownika do projektu
        mockMvc.perform(post("/project/" + project.getId() + "/user/" + user.getId()))
                .andExpect(status().isOk());

        // (d) Zweryfikuj, że użytkownik został przypisany
        Optional<Project> updatedProject = projectRepository.findById(project.getId());
        assertThat(updatedProject).isPresent();
        assertThat(updatedProject.get().getUsers()).contains(user);
    }
}
