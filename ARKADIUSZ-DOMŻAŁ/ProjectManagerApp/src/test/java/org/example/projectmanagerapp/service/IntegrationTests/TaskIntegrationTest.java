package org.example.projectmanagerapp.service.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.entity.Tasks.TaskType;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Projects testProject;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();

        testProject = new Projects();
        testProject.setName("Test_Tasks");
        testProject = projectRepository.save(testProject);

        Tasks t1 = new Tasks();
        t1.setTitle("Test_Task_1");
        t1.setDescription("Desc_1");
        t1.setTaskType(TaskType.LOW_PRIORITY);
        t1.setProject(testProject);

        Tasks t2 = new Tasks();
        t2.setTitle("Test_Task_2");
        t2.setDescription("Desc_2");
        t2.setTaskType(TaskType.HIGH_PRIORITY);
        t2.setProject(testProject);

        taskRepository.saveAll(List.of(t1, t2));
    }

    @AfterEach
    void cleanUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
    }


    @Test
    void shouldReturnAllTasks() throws Exception {
        mockMvc.perform(get("/api/tasks/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test_Task_1")))
                .andExpect(jsonPath("$[1].taskType", is("HIGH_PRIORITY")));
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        Tasks task = new Tasks();
        task.setTitle("Test_Task_3");
        task.setDescription("Desc_3");
        task.setTaskType(TaskType.MEDIUM_PRIORITY);
        task.setProject(testProject);
        task = taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Task found: Test_Task_3")));
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        Tasks newTask = new Tasks();
        newTask.setTitle("New_Test_Task");
        newTask.setDescription("Test_Task_Desc");
        newTask.setTaskType(TaskType.HIGH_PRIORITY);
        newTask.setProject(testProject);

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New_Test_Task")));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Tasks task = new Tasks();
        task.setTitle("To_change");
        task.setDescription("Desc");
        task.setTaskType(TaskType.LOW_PRIORITY);
        task.setProject(testProject);
        task = taskRepository.save(task);

        task.setTitle("Changed_title");

        mockMvc.perform(put("/api/tasks/update/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task updated successfully"));
    }


    @Test
    void shouldDeleteTask() throws Exception {
        Tasks task = new Tasks();
        task.setTitle("To_delete");
        task.setDescription("Desc");
        task.setTaskType(TaskType.MEDIUM_PRIORITY);
        task.setProject(testProject);
        task = taskRepository.save(task);

        mockMvc.perform(delete("/api/tasks/delete/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

    //teraz coveruje przypadki not-found

    @Test
    void shouldReturnNotFoundForNonexistentTask() throws Exception {
        mockMvc.perform(get("/api/tasks/7861"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with id: 7861 not found"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentTask() throws Exception {
        Tasks task = new Tasks();
        task.setTitle("not_existing");
        task.setDescription("desc");
        task.setTaskType(TaskType.HIGH_PRIORITY);
        task.setProject(testProject);

        mockMvc.perform(put("/api/tasks/update/7861")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with id: 7861 not found"));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/delete/7861"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }


}