package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.service.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class IntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public IntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }
    @Test
    @DisplayName("CreateUserAndProject")
    void CreateUserAndProject() throws Exception{
        Users users = new Users();
        users.setUsername("Edward");

        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Users savedUser = objectMapper.readValue(userResponse, Users.class);

        mockMvc.perform(get("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(status().is2xxSuccessful());

        Project project = new Project();
        project.setName("New Project");

        Set<Users> usersSet = new HashSet<>();
        usersSet.add(savedUser);
        project.setProjectUsers(usersSet);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(jsonPath("$.projectUsers[0].id", is(1)))
                .andExpect(jsonPath("$.projectUsers[0].username", is("Edward")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("CreateProjectAndTask")
    void CreateProjectAndTask() throws Exception {
        Project project = new Project();
        project.setName("New Project");

        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andReturn().getResponse().getContentAsString();

        Project savedProject = objectMapper.readValue(projectResponse, Project.class);

        Tasks tasks = new Tasks();
        tasks.setTitle("Task 1");
        tasks.setDescription("This is the first task");
        tasks.setTask_type(Priority.HIGH);

        tasks.setProject(savedProject);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tasks)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(jsonPath("$.project.id", is(1)))
                .andExpect(jsonPath("$.project.name", is("New Project")))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    @DisplayName("CreateAndTestAll")
    void CreateAndTestAll() throws Exception {
        Users users = new Users();
        users.setUsername("Edward");

        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andExpect(jsonPath("$.username", is("Edward")))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        Users savedUser = objectMapper.readValue(userResponse, Users.class);

        Project project = new Project();
        project.setName("New Project");

        Set<Users> usersSet = new HashSet<>();
        usersSet.add(savedUser);
        project.setProjectUsers(usersSet);

        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andReturn().getResponse().getContentAsString();

        Project savedProject = objectMapper.readValue(projectResponse, Project.class);

        Tasks tasks = new Tasks();
        tasks.setTitle("Task 1");
        tasks.setDescription("This is the first task");
        tasks.setTask_type(Priority.HIGH);

        tasks.setProject(savedProject);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tasks)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(jsonPath("$.project.id", is(1)))
                .andExpect(jsonPath("$.project.name", is("New Project")))
                .andExpect(jsonPath("$.project.projectUsers[0].id", is(1)))
                .andExpect(jsonPath("$.project.projectUsers[0].username", is("Edward")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("UpdateUserProjectAndTask")
    void updateUserProjectAndTask() throws Exception {
        Users user = new Users();
        user.setUsername("Edward");
        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();
        Users savedUser = objectMapper.readValue(userResponse, Users.class);

        savedUser.setUsername("EdwardUpdated");
        mockMvc.perform(put("/api/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("EdwardUpdated")));

        Project project = new Project();
        project.setName("Project A");
        project.setProjectUsers(Set.of(savedUser));
        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();
        Project savedProject = objectMapper.readValue(projectResponse, Project.class);

        savedProject.setName("Updated Project A");
        mockMvc.perform(put("/api/projects/{id}", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedProject)))
                .andExpect(jsonPath("$.name", is("Updated Project A")))
                .andExpect(status().is2xxSuccessful());

        Tasks task = new Tasks();
        task.setTitle("Initial Task");
        task.setDescription("Initial Description");
        task.setTask_type(Priority.MEDIUM);
        task.setProject(savedProject);
        String taskResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();
        Tasks savedTask = objectMapper.readValue(taskResponse, Tasks.class);


        savedTask.setTitle("Updated Task");
        savedTask.setDescription("Updated Description");
        savedTask.setTask_type(Priority.LOW);
        mockMvc.perform(put("/api/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTask)))
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.task_type", is("LOW")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("DeleteUserProjectAndTask")
    void deleteUserProjectAndTask() throws Exception {
        Users user = new Users();
        user.setUsername("Edward");
        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();
        Users savedUser = objectMapper.readValue(userResponse, Users.class);

        Project project = new Project();
        project.setName("Project B");
        project.setProjectUsers(Set.of(savedUser));
        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();
        Project savedProject = objectMapper.readValue(projectResponse, Project.class);

        Tasks task = new Tasks();
        task.setTitle("Task To Delete");
        task.setDescription("Task Desc");
        task.setTask_type(Priority.HIGH);
        task.setProject(savedProject);
        String taskResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();
        Tasks savedTask = objectMapper.readValue(taskResponse, Tasks.class);

        mockMvc.perform(delete("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/projects/{id}", savedProject.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/projects/{id}", savedProject.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()))
                .andExpect(status().isNotFound());
    }

}
