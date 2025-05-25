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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProjectIntegrationTest {

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
    public void testAssignUserToProject() throws Exception {
        Users user = new Users();
        user.setUsername("testUser");

        String userJson = objectMapper.writeValueAsString(user);
        MvcResult userResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

        Users createdUser = objectMapper.readValue(userResult.getResponse().getContentAsString(), Users.class);
        assertNotNull(createdUser.getId());
        Project project = new Project();
        project.setName("testProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);
        assertNotNull(createdProject.getId());

        mockMvc.perform(post("/api/projects/{id}/users", createdProject.getId())
                        .param("userId", createdUser.getId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/{id}/users", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testCrudOperations() throws Exception {
        Project project = new Project();
        project.setName("crudProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult createResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(createResult.getResponse().getContentAsString(), Project.class);
        assertNotNull(createdProject.getId());
        assertEquals("crudProject", createdProject.getName());

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + createdProject.getId() + ")].name").value("crudProject"));

        createdProject.setName("updatedProject");
        String updatedProjectJson = objectMapper.writeValueAsString(createdProject);
        mockMvc.perform(put("/api/projects/{id}", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updatedProject"));

        mockMvc.perform(delete("/api/projects/{id}", createdProject.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + createdProject.getId() + ")]").isEmpty());
    }

    @Test
    public void testDatabaseRelationships() throws Exception {
        Users user = new Users();
        user.setUsername("relationUser");

        String userJson = objectMapper.writeValueAsString(user);
        MvcResult userResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

        Users createdUser = objectMapper.readValue(userResult.getResponse().getContentAsString(), Users.class);

        Project project1 = new Project();
        project1.setName("relationProject1");
        String project1Json = objectMapper.writeValueAsString(project1);
        MvcResult project1Result = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(project1Json))
                .andExpect(status().isOk())
                .andReturn();
        Project createdProject1 = objectMapper.readValue(project1Result.getResponse().getContentAsString(), Project.class);

        Project project2 = new Project();
        project2.setName("relationProject2");
        String project2Json = objectMapper.writeValueAsString(project2);
        MvcResult project2Result = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(project2Json))
                .andExpect(status().isOk())
                .andReturn();
        Project createdProject2 = objectMapper.readValue(project2Result.getResponse().getContentAsString(), Project.class);

        mockMvc.perform(post("/api/projects/{id}/users", createdProject1.getId())
                        .param("userId", createdUser.getId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/projects/{id}/users", createdProject2.getId())
                        .param("userId", createdUser.getId().toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/{id}/users", createdProject1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/api/projects/{id}/users", createdProject2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
