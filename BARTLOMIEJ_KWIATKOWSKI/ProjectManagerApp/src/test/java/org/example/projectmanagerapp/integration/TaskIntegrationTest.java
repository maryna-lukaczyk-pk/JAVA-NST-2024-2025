package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.tasks.Tasks;
import org.example.projectmanagerapp.entity.tasks.TaskType;
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
class TaskIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanup() {
        jdbcTemplate.update("DELETE FROM project_users");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM tasks");
        jdbcTemplate.update("DELETE FROM project");
    }

    @Test
    void taskCrudFlow() throws Exception {
        // Najpierw stwórz projekt - zadanie musi być przypisane do projektu
        Project project = new Project();
        project.setName("project_" + System.currentTimeMillis());
        project.setDescription("desc");
        String projectJson = objectMapper.writeValueAsString(project);

        String projResp = mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long projectId = objectMapper.readValue(projResp, Project.class).getId();

        // CREATE
        Tasks task = new Tasks();
        task.setTitle("task_" + System.currentTimeMillis());
        task.setDescription("desc");
        task.setTaskType(TaskType.HIGH_PRIORITY);

        Project projectRef = new Project();
        projectRef.setId(projectId);
        task.setProject(projectRef);

        String taskJson = objectMapper.writeValueAsString(task);

        String resp = mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(resp, Tasks.class).getId();

        // READ
        mvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(task.getTitle())));

        // UPDATE
        Tasks upd = new Tasks();
        upd.setTitle("updTask_" + System.currentTimeMillis());
        upd.setDescription("updated desc");
        upd.setTaskType(TaskType.LOW_PRIORITY);
        Project updProj = new Project();
        updProj.setId(projectId);
        upd.setProject(updProj);

        String updJson = objectMapper.writeValueAsString(upd);

        mvc.perform(put("/api/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(upd.getTitle())));

        // DELETE
        mvc.perform(delete("/api/tasks/" + id))
                .andExpect(status().isNoContent());

        // NOT FOUND
        mvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isNotFound());
    }
}