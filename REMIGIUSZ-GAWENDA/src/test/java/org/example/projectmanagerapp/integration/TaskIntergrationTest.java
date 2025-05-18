package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create, update and delete a task")
    void taskCrudFlow() throws Exception {
        // Step 1: Create Project for Task
        Project project = new Project("Task Project", "For task test", "ACTIVE");
        String projectJson = objectMapper.writeValueAsString(project);

        String projectResp = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Project savedProject = objectMapper.readValue(projectResp, Project.class);

        // Step 2: Create Task
        Task task = new Task("Initial Task", "Details", TaskType.HIGH_PRIORITY, savedProject);
        String taskJson = objectMapper.writeValueAsString(task);

        String taskResp = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Task savedTask = objectMapper.readValue(taskResp, Task.class);

        // Step 3: Update Task
        savedTask.setTitle("Updated Task");
        savedTask.setDescription("Updated Description");

        String updatedJson = objectMapper.writeValueAsString(savedTask);

        mockMvc.perform(put("/api/tasks/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        // Step 4: Delete Task
        mockMvc.perform(delete("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isOk());
    }
}
