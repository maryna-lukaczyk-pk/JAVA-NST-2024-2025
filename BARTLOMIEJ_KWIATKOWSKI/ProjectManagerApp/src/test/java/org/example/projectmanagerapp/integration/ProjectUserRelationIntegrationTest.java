package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ProjectUserRelationIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanup() {
        jdbcTemplate.update("DELETE FROM project_users");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM project");
        jdbcTemplate.update("DELETE FROM tasks");
    }

    @Test
    void shouldAddUserToProject() throws Exception {
        // Utwórz użytkownika
        Users user = new Users();
        user.setUsername("reluser_" + System.currentTimeMillis());
        String userJson = objectMapper.writeValueAsString(user);
        String userResp = mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long userId = objectMapper.readValue(userResp, Users.class).getId();

        // Utwórz projekt
        Project project = new Project();
        project.setName("relproject_" + System.currentTimeMillis());
        project.setDescription("desc");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResp = mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long projectId = objectMapper.readValue(projectResp, Project.class).getId();

        // Przypisz użytkownika do projektu
        mvc.perform(put("/api/projects/" + projectId + "/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())));

        // Zweryfikuj, że użytkownik jest na liście członków projektu
        mvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(userId.intValue())));
    }
}