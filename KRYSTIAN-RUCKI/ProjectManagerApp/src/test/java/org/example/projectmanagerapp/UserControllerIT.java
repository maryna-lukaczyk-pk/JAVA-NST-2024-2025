package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.repository.ProjectRepository;
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
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIT {

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
        Users user = new Users();
        user.setUsername("new-user");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new-user"));

        assertTrue(userRepository.findAll().stream().anyMatch(u -> u.getUsername().equals("new-user")));
    }

    @Test
    @DisplayName("Should get all users")
    void testGetAllUsers() throws Exception {
        userRepository.deleteAll();

        Users userA = new Users();
        userA.setUsername("userA");
        userRepository.save(userA);

        Users userB = new Users();
        userB.setUsername("userB");
        userRepository.save(userB);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should get user by ID")
    void testGetUserById() throws Exception {
        Users user = new Users();
        user.setUsername("userToFind");
        Users saved = userRepository.save(user);

        mockMvc.perform(get("/users/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("userToFind"));
    }

    @Test
    @DisplayName("Should update an already existing user")
    void testUpdateUser() throws Exception {
        Users user = new Users();
        user.setUsername("before_update");
        Users saved = userRepository.save(user);

        saved.setUsername("after_update");

        mockMvc.perform(put("/users/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("after_update"));
    }

    @Test
    @DisplayName("Should delete an user")
    void testDeleteUser() throws Exception {
        Users user = new Users();
        user.setUsername("delete_this_user");
        Users saved = userRepository.save(user);

        mockMvc.perform(delete("/users/" + saved.getId()))
                .andExpect(status().isOk());

        Optional<Users> deleted = userRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }

    @Transactional
    @Test
    @DisplayName("Should create a new user with assigned project")
    void testCreateUserWithProject() throws Exception {
        Project project = new Project();
        project.setName("TestProject");
        Project savedProject = projectRepository.save(project);

        Users user = new Users();
        user.setUsername("user_with_project");
        user.setProjects(Set.of(savedProject));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user_with_project"))
                .andExpect(jsonPath("$.projects[0].name").value("TestProject"));

        Users createdUser = userRepository.findAll().stream()
                .filter(u -> "user_with_project".equals(u.getUsername()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("User not found"));

        assertTrue(createdUser.getProjects().stream()
                .anyMatch(p -> "TestProject".equals(p.getName())));
    }
}