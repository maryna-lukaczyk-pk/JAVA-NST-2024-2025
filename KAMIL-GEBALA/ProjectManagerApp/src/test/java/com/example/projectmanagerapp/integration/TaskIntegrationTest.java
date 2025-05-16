package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.config.TestConfig;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.priority.HighPriority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTask() throws Exception {
        Project project = new Project();
        project.setName("testTaskProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Tasks task = new Tasks();
        task.setTitle("testCreateTask");
        task.setDescription("Test task description");
        task.setTask_type(Tasks.TaskType.NEW);
        task.setPriorityLevel(new HighPriority());
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("testCreateTask")))
                .andExpect(jsonPath("$.description", is("Test task description")))
                .andExpect(jsonPath("$.task_type", is("NEW")))
                .andExpect(jsonPath("$.priority", is("HIGH")));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Project project = new Project();
        project.setName("testGetAllTasksProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Tasks task = new Tasks();
        task.setTitle("testGetAllTasks");
        task.setDescription("Test get all tasks");
        task.setTask_type(Tasks.TaskType.NEW);
        task.setPriorityLevel(new HighPriority());
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tasks/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].title", hasItem("testGetAllTasks")));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Project project = new Project();
        project.setName("testUpdateTaskProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Tasks task = new Tasks();
        task.setTitle("testUpdateTaskBefore");
        task.setDescription("Test update task before");
        task.setTask_type(Tasks.TaskType.NEW);
        task.setPriorityLevel(new HighPriority());
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        MvcResult taskResult = mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn();

        Tasks createdTask = objectMapper.readValue(
                taskResult.getResponse().getContentAsString(), Tasks.class);

        createdTask.setTitle("testUpdateTaskAfter");
        createdTask.setDescription("Test update task after");
        createdTask.setTask_type(Tasks.TaskType.IN_PROGRESS);

        String updatedTaskJson = objectMapper.writeValueAsString(createdTask);
        mockMvc.perform(put("/api/tasks/update/" + createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("testUpdateTaskAfter")))
                .andExpect(jsonPath("$.description", is("Test update task after")))
                .andExpect(jsonPath("$.task_type", is("IN_PROGRESS")));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Project project = new Project();
        project.setName("testDeleteTaskProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Tasks task = new Tasks();
        task.setTitle("testDeleteTask");
        task.setDescription("Test delete task");
        task.setTask_type(Tasks.TaskType.NEW);
        task.setPriorityLevel(new HighPriority());
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        MvcResult taskResult = mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn();

        Tasks createdTask = objectMapper.readValue(
                taskResult.getResponse().getContentAsString(), Tasks.class);

        mockMvc.perform(delete("/api/tasks/delete/" + createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", not(hasItem("testDeleteTask"))));
    }
}

