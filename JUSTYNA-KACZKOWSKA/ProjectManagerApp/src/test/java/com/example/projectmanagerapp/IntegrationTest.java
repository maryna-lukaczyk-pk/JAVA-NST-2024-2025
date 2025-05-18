package com.example.projectmanagerapp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAssignUserToProject() throws Exception {
        User user = new User();
        user.setUsername("Testowy User1");
        String jsonUser = objectMapper.writeValueAsString(user);
        String createdUserResponse =  mvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        User createdUser = objectMapper.readValue(createdUserResponse, User.class);
        Long userId = createdUser.getId();
        Project project = new Project();
        project.setName("Testowy_Projekt123");
        String jsonProject = objectMapper.writeValueAsString(project);
        String createdProjectResponse =  mvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProject))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Project createdProject = objectMapper.readValue(createdProjectResponse, Project.class);
        Long projectId = createdProject.getId();
        mvc.perform(put("/api/projects/" + projectId + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userId)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Testowy_Projekt123"))
                .andExpect(jsonPath("$.users[0].username").value("Testowy User1"));
    }
    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("User");
        mvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("User"));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setUsername("UserById");
        String response = mvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();
        User created = objectMapper.readValue(response, User.class);
        mvc.perform(get("/api/users/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("UserById"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("UserToDelete");
        String response = mvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();
        User created = objectMapper.readValue(response, User.class);
        mvc.perform(delete("/api/users/" + created.getId()))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/users/" + created.getId()))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testCreateTask() throws Exception {
        Project project = new Project();
        project.setName("TestowyProjekt");

        String projectResponse = mvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("Zadanie testowe");
        task.setDescription("Opis testowy");
        task.setTask_type(TaskType.Issue);
        task.setProject(createdProject);

        mvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Zadanie testowe"));
    }
    @Test
    public void testGetTaskById() throws Exception {
        Project project = new Project();
        project.setName("Projekt 12");

        String projectResponse = mvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("Zadanie testowe");
        task.setDescription("Opis");
        task.setTask_type(TaskType.Bug);
        task.setProject(createdProject);

        String taskResponse = mvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();

        Task createdTask = objectMapper.readValue(taskResponse, Task.class);

        mvc.perform(get("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Zadanie testowe"));
    }



    @Test
    public void testDeleteTask() throws Exception {
        Project project = new Project();
        project.setName("Projekt 1234");

        String projectResponse = mvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("Zadabnie test usuniecie");
        task.setDescription("Opis test");
        task.setTask_type(TaskType.Feature);
        task.setProject(createdProject);

        String taskResponse = mvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();

        Task createdTask = objectMapper.readValue(taskResponse, Task.class);

        mvc.perform(delete("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/tasks/" + createdTask.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Project project = new Project();
        project.setName("Projekt 444");

        String projectResponse = mvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("Zadanie na liście");
        task.setDescription("Opis");
        task.setTask_type(TaskType.Bug);
        task.setProject(createdProject);

        mvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testUpdateTask() throws Exception {
        Project project = new Project();
        project.setName("Projekt 777");

        String projectResponse = mvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        Task task = new Task();
        task.setTitle("Zadanie edit");
        task.setDescription("Opis");
        task.setTask_type(TaskType.Issue);
        task.setProject(createdProject);

        String taskResponse = mvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();

        Task createdTask = objectMapper.readValue(taskResponse, Task.class);
        createdTask.setTitle("pop zadanie");

        mvc.perform(put("/api/tasks/" + createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("pop zadanie"));
    }

}



