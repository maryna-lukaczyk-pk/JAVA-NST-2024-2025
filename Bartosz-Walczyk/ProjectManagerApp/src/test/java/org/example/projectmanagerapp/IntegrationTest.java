package org.example.projectmanagerapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:13")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresql::getJdbcUrl);
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("spring.datasource.password", postgresql::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAssignUserToProject() throws Exception {
        User user = new User();
        user.setUsername("john.doe");
        String userJson = objectMapper.writeValueAsString(user);
        String userResponse = mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        User createdUser = objectMapper.readValue(userResponse, User.class);

        Project project = new Project();
        project.setName("Project X");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        ArrayList<User> users = new ArrayList<>();
        users.add(createdUser);
        createdProject.setUsers(users);
        projectJson = objectMapper.writeValueAsString(createdProject);
        mockMvc.perform(put("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(projectJson))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + createdProject.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users[0].id").value(createdUser.getId()));
    }

    @Test
    void testAssignTaskToProject() throws Exception {
        Project project = new Project();
        project.setName("Project Y");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Description");
        task.setTask_type(null);
        task.setProject(createdProject);
        String taskJson = objectMapper.writeValueAsString(task);
        String taskResponse = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Task createdTask = objectMapper.readValue(taskResponse, Task.class);

        mockMvc.perform(get("/api/projects/" + createdProject.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tasks[0].id").value(createdTask.getId()));
    }

    @Test
    void testTaskHasProject() throws Exception {
        Project project = new Project();
        project.setName("Project Z");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("Task 2");
        task.setDescription("Description");
        task.setTask_type(null);
        task.setProject(createdProject);
        String taskJson = objectMapper.writeValueAsString(task);
        String taskResponse = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(taskJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Task createdTask = objectMapper.readValue(taskResponse, Task.class);

        mockMvc.perform(get("/api/tasks/" + createdTask.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.project.id").value(createdProject.getId()));
    }
}
