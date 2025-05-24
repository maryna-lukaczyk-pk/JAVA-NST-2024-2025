package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProjectControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private Project testProject1;
    private Project testProject2;
    private User testUser;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("TestUser");
        userRepository.save(testUser);

        testProject1 = new Project();
        testProject1.setName("Test Project 1");
        testProject1.setUsers(new ArrayList<>());

        testProject2 = new Project();
        testProject2.setName("Test Project 2");
        testProject2.setUsers(new ArrayList<>());

        projectRepository.saveAll(List.of(testProject1, testProject2));
    }

    @Test
    void getAllProjects_ReturnsListOfProjects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(testProject1.getName())))
                .andExpect(jsonPath("$[1].name", is(testProject2.getName())));
    }

    @Test
    void getProjectById_ReturnsProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/{id}", testProject1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testProject1.getName())));
    }

    @Test
    void getProjectById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProject_ReturnsCreatedProject() throws Exception {
        Project newProject = new Project();
        newProject.setName("New Project");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newProject.getName())));
    }

    @Test
    void updateProject_ReturnsUpdatedProject() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setName("Updated Project Name");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/projects/update/{id}", testProject1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedProject.getName())));
    }

    @Test
    void updateProject_NotFound() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setName("Updated Project Name");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/projects/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProject_ReturnsOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/projects/delete/{id}", testProject1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/{id}", testProject1.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProject_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/projects/delete/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUserToProject_ReturnsProjectWithUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/addUser/{id}", testProject1.getId())
                        .param("userId", testUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testProject1.getName())))
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.users[0].username", is(testUser.getUsername())));
    }

    @Test
    void addUserToProject_ProjectNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/addUser/{id}", 999L)
                        .param("userId", testUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUserToProject_UserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/addUser/{id}", testProject1.getId())
                        .param("userId", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void removeUserFromProject_ReturnsProjectWithoutUser() throws Exception {
        projectService.addUserToProject(testProject1.getId(), testUser.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/removeUser/{id}", testProject1.getId())
                        .param("userId", testUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testProject1.getName())))
                .andExpect(jsonPath("$.users", hasSize(0)));
    }

    @Test
    void removeUserFromProject_ProjectNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/removeUser/{id}", 999L)
                        .param("userId", testUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeUserFromProject_UserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/removeUser/{id}", testProject1.getId())
                        .param("userId", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeUserFromProject_UserNotInProject() throws Exception {
        User anotherUser = new User();
        anotherUser.setUsername("AnotherUser");
        userRepository.save(anotherUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects/removeUser/{id}", testProject1.getId())
                        .param("userId", anotherUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testProject1.getName())))
                .andExpect(jsonPath("$.users", hasSize(0)));
    }
}