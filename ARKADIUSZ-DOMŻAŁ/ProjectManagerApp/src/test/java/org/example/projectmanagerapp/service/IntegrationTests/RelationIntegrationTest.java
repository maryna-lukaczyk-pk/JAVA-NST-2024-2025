package org.example.projectmanagerapp.service.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.controller.ProjectsController;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Users;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testAssignTaskstoProject() throws Exception {
        String projectJson = "{\"name\": \"Test Project\"}";

        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Projects createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Projects.class
        );

        String task1Json = String.format("{\"title\":\"Task 1\",\"project\":{\"id\":%d}}", createdProject.getId());
        String task2Json = String.format("{\"title\":\"Task 2\",\"project\":{\"id\":%d}}", createdProject.getId());

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(task1Json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(task2Json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/projects/" + createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks.length()").value(2))
                .andExpect(jsonPath("$.tasks[?(@.title == 'Task 1')]").exists())
                .andExpect(jsonPath("$.tasks[?(@.title == 'Task 2')]").exists());
    }

    @Test
    public void testAssignUserToProject() throws Exception {
        // tworze usera przez api
        Users user = new Users();
        user.setUsername("UserForAssignment");

        String userResponse = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Users createdUser = objectMapper.readValue(userResponse, Users.class);

        // tworze projekt prez api
        Projects project = new Projects();
        project.setName("ProjectForAssignment");

        String projectResponse = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Projects createdProject = objectMapper.readValue(projectResponse, Projects.class);

        // Przypisuje usera do projektu
        ProjectsController.AssignUserRequest assignRequest =
                new ProjectsController.AssignUserRequest(createdUser.getId());

        mockMvc.perform(post("/api/projects/" + createdProject.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignRequest)))
                .andExpect(status().isCreated());

        // sprawdzam czy uzytkownicy zostali przypisani do projektu pobierajac dane projektu
        mockMvc.perform(get("/api/projects/" + createdProject.getId() + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(createdUser.getId()))
                .andExpect(jsonPath("$[0].username").value("UserForAssignment"));
    }

    @Test
    public void testGetProjectsForUser() throws Exception {
        //Tworze użytkownika
        Users user = new Users();
        user.setUsername("ProjectUser");

        String userResponse = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Users createdUser = objectMapper.readValue(userResponse, Users.class);

        // Tworze projekt
        Projects project = new Projects();
        project.setName("AssignedProject");

        String projectResponse = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Projects createdProject = objectMapper.readValue(projectResponse, Projects.class);

        // Przypisuje uytkownika do projektu
        ProjectsController.AssignUserRequest assignRequest =
                new ProjectsController.AssignUserRequest(createdUser.getId());

        mockMvc.perform(post("/api/projects/" + createdProject.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignRequest)))
                .andExpect(status().isCreated());

        // pobieram projekty przypisane do uzytkownika, i sprawdzam czy jest przypisany
        mockMvc.perform(get("/api/users/" + createdUser.getId() + "/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(createdProject.getId()))
                .andExpect(jsonPath("$[0].name").value("AssignedProject"));
    }

    @Test
    public void testGetUsersAssignedToProject() throws Exception {
        // tworze projekt
        Projects project = new Projects();
        project.setName("ProjectWithMultipleUsers");

        String projectResponse = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Projects createdProject = objectMapper.readValue(projectResponse, Projects.class);

        // teraz dwóch uzytkownikow
        Users user1 = new Users();
        user1.setUsername("UserOne");
        String user1Response = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Users createdUser1 = objectMapper.readValue(user1Response, Users.class);

        Users user2 = new Users();
        user2.setUsername("UserTwo");
        String user2Response = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Users createdUser2 = objectMapper.readValue(user2Response, Users.class);

        // przypisuje ich do projektu
        ProjectsController.AssignUserRequest assignRequest1 = new ProjectsController.AssignUserRequest(createdUser1.getId());
        ProjectsController.AssignUserRequest assignRequest2 = new ProjectsController.AssignUserRequest(createdUser2.getId());

        mockMvc.perform(post("/api/projects/" + createdProject.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignRequest1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/projects/" + createdProject.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignRequest2)))
                .andExpect(status().isCreated());

        // pobieram uzytkowników i sprawdzam, czy są przypisani do tego projektu
        mockMvc.perform(get("/api/projects/" + createdProject.getId() + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %d)].username", createdUser1.getId()).value("UserOne"))
                .andExpect(jsonPath("$[?(@.id == %d)].username", createdUser2.getId()).value("UserTwo"));
    }



}