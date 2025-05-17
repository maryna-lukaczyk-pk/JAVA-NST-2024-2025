package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("new_user");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_user"));

        assertTrue(userRepository.findAll().stream().anyMatch(u -> u.getUsername().equals("new_user")));
    }

    @Test
    @DisplayName("Should get all users")
    void testGetAllUsers() throws Exception {
        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should get user by ID")
    void testGetUserById() throws Exception {
        User user = new User();
        user.setUsername("find_me");
        User saved = userRepository.save(user);

        mockMvc.perform(get("/users/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("find_me"));
    }

    @Test
    @DisplayName("Should update an existing user")
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("before_update");
        User saved = userRepository.save(user);

        saved.setUsername("after_update");

        mockMvc.perform(put("/users/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("after_update"));
    }

    @Test
    @DisplayName("Should delete a user")
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("delete_me");
        User saved = userRepository.save(user);

        mockMvc.perform(delete("/users/" + saved.getId()))
                .andExpect(status().isOk());

        Optional<User> deleted = userRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }

    @Transactional
    @Test
    @DisplayName("Should create a new user with assigned project")
    void testCreateUserWithProject() throws Exception {
        Project project = new Project();
        project.setName("Test Project");
        Project savedProject = projectRepository.save(project);

        User user = new User();
        user.setUsername("user_with_project");
        user.setProjects(Set.of(savedProject));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user_with_project"))
                .andExpect(jsonPath("$.projects[0].name").value("Test Project"));

        User createdUser = userRepository.findAll().stream()
                .filter(u -> "user_with_project".equals(u.getUsername()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("User not found"));

        assertTrue(createdUser.getProjects().stream()
                .anyMatch(p -> "Test Project".equals(p.getName())));
    }
}
