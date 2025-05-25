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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserIntegrationTest {

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
    public void testUserCrudOperations() throws Exception {
        Users user = new Users();
        user.setUsername("testUser");

        String userJson = objectMapper.writeValueAsString(user);
        MvcResult createResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

        Users createdUser = objectMapper.readValue(createResult.getResponse().getContentAsString(), Users.class);
        assertNotNull(createdUser.getId());
        assertEquals("testUser", createdUser.getUsername());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.id == " + createdUser.getId() + ")].username").value("testUser"));

        createdUser.setUsername("updatedUser");
        String updatedUserJson = objectMapper.writeValueAsString(createdUser);
        mockMvc.perform(put("/api/users/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));

        mockMvc.perform(delete("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + createdUser.getId() + ")]").isEmpty());
    }

    @Test
    public void testUserProjectRelationships() throws Exception {
        Users user1 = new Users();
        user1.setUsername("User1");
        String user1Json = objectMapper.writeValueAsString(user1);
        MvcResult user1Result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user1Json))
                .andExpect(status().isOk())
                .andReturn();
        Users createdUser1 = objectMapper.readValue(user1Result.getResponse().getContentAsString(), Users.class);

        Users user2 = new Users();
        user2.setUsername("User2");
        String user2Json = objectMapper.writeValueAsString(user2);
        MvcResult user2Result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user2Json))
                .andExpect(status().isOk())
                .andReturn();
        Users createdUser2 = objectMapper.readValue(user2Result.getResponse().getContentAsString(), Users.class);

        Project project = new Project();
        project.setName("Shared Project");
        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();
        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);

        mockMvc.perform(post("/api/projects/{id}/users", createdProject.getId())
                        .param("userId", createdUser1.getId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/projects/{id}/users", createdProject.getId())
                        .param("userId", createdUser2.getId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/{id}/users", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(delete("/api/projects/{id}/users/{userId}", createdProject.getId(), createdUser1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/{id}/users", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testInvalidUserOperations() throws Exception {
        Users nonExistentUser = new Users();
        nonExistentUser.setUsername("NonExistentUser");
        String userJson = objectMapper.writeValueAsString(nonExistentUser);

        mockMvc.perform(put("/api/users/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(delete("/api/users/{id}", 999L))
                .andExpect(status().isOk());
    }
}
