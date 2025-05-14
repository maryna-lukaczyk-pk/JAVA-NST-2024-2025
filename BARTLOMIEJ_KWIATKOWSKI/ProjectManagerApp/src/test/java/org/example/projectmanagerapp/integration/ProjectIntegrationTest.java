package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Tag("integration")
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ProjectIntegrationTest {

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
    void projectCrudFlow() throws Exception {
        // CREATE
        Project project = new Project();
        project.setName("project_" + System.currentTimeMillis());
        project.setDescription("desc");
        String projectJson = objectMapper.writeValueAsString(project);

        String resp = mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(resp, Project.class).getId();

        // READ
        mvc.perform(get("/api/projects/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(project.getName())));

        // UPDATE
        Project upd = new Project();
        upd.setName("updProject_" + System.currentTimeMillis());
        upd.setDescription("Updated desc");
        String updJson = objectMapper.writeValueAsString(upd);

        mvc.perform(put("/api/projects/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(upd.getName())));

        // DELETE
        mvc.perform(delete("/api/projects/" + id))
                .andExpect(status().isNoContent());

        // NOT FOUND
        mvc.perform(get("/api/projects/" + id))
                .andExpect(status().isNotFound());
    }
}