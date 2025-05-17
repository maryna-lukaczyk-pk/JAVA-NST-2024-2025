package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.projectmanagerapp.dto.CreateTaskRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void testCreateAndGetTask() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "Test Task",
                "Description for test task",
                TaskType.MEDIUM_PRIORITY
        );

        // Create a task
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Task")))
                .andExpect(jsonPath("$.description", is("Description for test task")))
                .andExpect(jsonPath("$.taskType", is("MEDIUM_PRIORITY")));

        // Get all tasks and verify the one created is present
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Task")));
    }

    @Test
    void testUpdateTask() throws Exception {
        Tasks savedTask = taskRepository.save(new Tasks(
                null,
                "Old Title",
                "Old Description",
                TaskType.LOW_PRIORITY,
                null,
                null
        ));

        CreateTaskRequest updateRequest = new CreateTaskRequest(
                "Updated Title",
                "Updated Description",
                TaskType.HIGH_PRIORITY
        );

        mockMvc.perform(put("/api/tasks/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.taskType", is("HIGH_PRIORITY")));
    }

    @Test
    void testDeleteTask() throws Exception {
        Tasks task = taskRepository.save(new Tasks(
                null,
                "To be deleted",
                "Description",
                TaskType.MEDIUM_PRIORITY,
                null,
                null
        ));

        mockMvc.perform(delete("/api/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));

        // Verify task deleted
        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    @Test
    void testAssignTaskToProject() throws Exception {
        // Create a project
            var project = projectRepository.save(new Project(null, "Test Project", new HashSet<>(), new ArrayList<>()));

        // Create a task
        var task = taskRepository.save(new Tasks(
                null,
                "Task to assign",
                "Description",
                TaskType.HIGH_PRIORITY,
                null,
                null
        ));

        mockMvc.perform(post("/api/tasks/" + task.getId() + "/projects/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Project assigned to user successfully."));

        // Verify a task has a project assigned
        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project.id", is(project.getId().intValue())))
                .andExpect(jsonPath("$.project.name", is("Test Project")));
    }
}
