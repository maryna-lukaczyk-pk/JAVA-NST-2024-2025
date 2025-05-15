package com.example.demo.integration;

import com.example.demo.entity.Project;
import com.example.demo.entity.ProjectUsers;
import com.example.demo.entity.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProjectUsersIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAssignAndRemoveMultipleUsersToProject() throws Exception {

        Project project = new Project();
        project.setName("Multi-User Project");
        
        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();
        
        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);
        

        Users[] users = new Users[5];
        for (int i = 0; i < 5; i++) {
            Users user = new Users();
            user.setUsername("User" + i);
            
            String userJson = objectMapper.writeValueAsString(user);
            MvcResult userResult = mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isOk())
                    .andReturn();
            
            users[i] = objectMapper.readValue(userResult.getResponse().getContentAsString(), Users.class);
        }
        

        for (Users user : users) {
            mockMvc.perform(post("/api/projects/{id}/users", createdProject.getId())
                            .param("userId", user.getId().toString()))
                    .andExpect(status().isOk());
        }
        

        mockMvc.perform(get("/api/projects/{id}/users", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
        

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(delete("/api/projects/{id}/users/{userId}", createdProject.getId(), users[i].getId()))
                    .andExpect(status().isOk());
            
            mockMvc.perform(get("/api/projects/{id}/users", createdProject.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(4 - i)));
        }
    }

    @Test
    public void testAssignSameUserTwice() throws Exception {

        Project project = new Project();
        project.setName("Duplicate User Project");
        
        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();
        
        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);
        

        Users user = new Users();
        user.setUsername("DuplicateUser");
        
        String userJson = objectMapper.writeValueAsString(user);
        MvcResult userResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();
        
        Users createdUser = objectMapper.readValue(userResult.getResponse().getContentAsString(), Users.class);
        

        MvcResult assignResult1 = mockMvc.perform(post("/api/projects/{id}/users", createdProject.getId())
                        .param("userId", createdUser.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();
        
        ProjectUsers projectUser1 = objectMapper.readValue(assignResult1.getResponse().getContentAsString(), ProjectUsers.class);
        assertNotNull(projectUser1);
        

        MvcResult assignResult2 = mockMvc.perform(post("/api/projects/{id}/users", createdProject.getId())
                        .param("userId", createdUser.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();
        
        ProjectUsers projectUser2 = objectMapper.readValue(assignResult2.getResponse().getContentAsString(), ProjectUsers.class);
        assertNotNull(projectUser2);
        

        mockMvc.perform(get("/api/projects/{id}/users", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testInvalidProjectUserOperations() throws Exception {
        mockMvc.perform(post("/api/projects/{id}/users", 999L)
                        .param("userId", "999"))
                .andExpect(status().isNotFound());
        
        Project project = new Project();
        project.setName("Invalid Operations Project");
        
        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();
        
        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);
        
        mockMvc.perform(post("/api/projects/{id}/users", createdProject.getId())
                        .param("userId", "999"))
                .andExpect(status().isNotFound());
        
        Users user = new Users();
        user.setUsername("InvalidOperationsUser");
        
        String userJson = objectMapper.writeValueAsString(user);
        MvcResult userResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();
        
        Users createdUser = objectMapper.readValue(userResult.getResponse().getContentAsString(), Users.class);
        
        mockMvc.perform(post("/api/projects/{id}/users", 999L)
                        .param("userId", createdUser.getId().toString()))
                .andExpect(status().isNotFound());
        
        mockMvc.perform(delete("/api/projects/{id}/users/{userId}", 999L, 999L))
                .andExpect(status().isOk());
        
        mockMvc.perform(delete("/api/projects/{id}/users/{userId}", createdProject.getId(), 999L))
                .andExpect(status().isOk());
        
        mockMvc.perform(delete("/api/projects/{id}/users/{userId}", 999L, createdUser.getId()))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/api/projects/{id}/users", 999L))
                .andExpect(status().isNotFound());
    }
}