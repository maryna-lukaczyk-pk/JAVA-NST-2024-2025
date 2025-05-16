package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.ProjectManagerAppApplication;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.schemas.ProjectDTO;
import org.example.projectmanagerapp.schemas.TaskDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Integer createProjectAndGetId(String name) throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(name);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(status().isCreated());

        MvcResult getAll = mockMvc.perform(get("/api/projects"))
                .andReturn();

        List<Map<String, Object>> projects = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        return (Integer) projects.get(projects.size() - 1).get("id");
    }

    @Test
    void shouldCreateTask() throws Exception {
        Integer projectId = createProjectAndGetId("TaskProject1");

        TaskDTO dto = new TaskDTO();
        dto.setTitle("Task1");
        dto.setDescription("Task1 desc");
        dto.setTaskType(TaskType.MEDIUM);
        dto.setProjectId(projectId);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Integer projectId = createProjectAndGetId("TaskProject2");

        TaskDTO dto = new TaskDTO();
        dto.setTitle("Task2");
        dto.setDescription("Task2 desc");
        dto.setTaskType(TaskType.LOW);
        dto.setProjectId(projectId);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        MvcResult getAll = mockMvc.perform(get("/api/tasks"))
                .andReturn();

        List<Map<String, Object>> tasks = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        Integer id = (Integer) tasks.get(tasks.size() - 1).get("id");

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Task2")))
                .andExpect(jsonPath("$.description", is("Task2 desc")))
                .andExpect(jsonPath("$.taskType", is("LOW")))
                .andExpect(jsonPath("$.project.id", is(projectId)));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Integer projectId = createProjectAndGetId("TaskProject3");

        TaskDTO dto = new TaskDTO();
        dto.setTitle("originalTask");
        dto.setDescription("originalTask desc");
        dto.setTaskType(TaskType.HIGH);
        dto.setProjectId(projectId);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        MvcResult getAll = mockMvc.perform(get("/api/tasks"))
                .andReturn();

        List<Map<String, Object>> tasks = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        Integer id = (Integer) tasks.get(tasks.size() - 1).get("id");

        TaskDTO updateDTO = new TaskDTO();
        updateDTO.setTitle("updatedTask");
        updateDTO.setTaskType(TaskType.MEDIUM);

        mockMvc.perform(patch("/api/tasks/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("updatedTask")))
                .andExpect(jsonPath("$.taskType", is("MEDIUM")));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Integer projectId = createProjectAndGetId("TaskProject4");

        TaskDTO dto = new TaskDTO();
        dto.setTitle("taskToDelete");
        dto.setDescription("taskToDelete desc");
        dto.setTaskType(TaskType.LOW);
        dto.setProjectId(projectId);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        MvcResult getAll = mockMvc.perform(get("/api/tasks"))
                .andReturn();

        List<Map<String, Object>> tasks = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer id = (Integer) tasks.get(tasks.size() - 1).get("id");

        mockMvc.perform(delete("/api/tasks/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isNotFound());
    }
}
