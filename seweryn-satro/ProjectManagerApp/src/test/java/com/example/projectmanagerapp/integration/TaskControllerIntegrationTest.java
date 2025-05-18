package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Project testProject;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();

        testProject = new Project();
        testProject.setName("ProjectForTask");
        testProject.setUsers(Set.of());
        projectRepository.save(testProject);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testAllTasks() throws Exception {
        Task taskOne = new Task();
        taskOne.setTitle("Task 1");
        taskOne.setProject(testProject);
        taskOne.setTask_type(TaskType.HIGH);
        taskOne.setPriority("HIGH");

        Task taskTwo = new Task();
        taskTwo.setTitle("Task 2");
        taskTwo.setProject(testProject);
        taskTwo.setTask_type(TaskType.LOW);
        taskTwo.setPriority("LOW");

        taskRepository.save(taskOne);
        taskRepository.save(taskTwo);

        mockMvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Task 1", "Task 2")));
    }

    @Test
    @DisplayName("Should create new task")
    void testNewTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("Task description");
        newTask.setTask_type(TaskType.HIGH);
        newTask.setPriority("MEDIUM");
        newTask.setProject(testProject);

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.task_type").value("HIGH"));
    }

    @Test
    @DisplayName("Should update task")
    void testUpdateTask() throws Exception {
        Task existingTask = new Task();
        existingTask.setTitle("Original Task");
        existingTask.setDescription("Original Desc");
        existingTask.setTask_type(TaskType.LOW);
        existingTask.setPriority("LOW");
        existingTask.setProject(testProject);
        Task savedTask = taskRepository.save(existingTask);

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Desc");
        updatedTask.setTask_type(TaskType.MEDIUM);
        updatedTask.setPriority("HIGH");
        updatedTask.setProject(testProject);

        mockMvc.perform(put("/api/tasks/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    @DisplayName("Should delete task")
    void testDeleteTask() throws Exception {
        Task taskToDelete = new Task();
        taskToDelete.setTitle("To be deleted");
        taskToDelete.setDescription("To be deleted desc");
        taskToDelete.setTask_type(TaskType.LOW);
        taskToDelete.setPriority("LOW");
        taskToDelete.setProject(testProject);
        Task savedTask = taskRepository.save(taskToDelete);

        mockMvc.perform(delete("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isOk());

        assertFalse(taskRepository.findById(savedTask.getId()).isPresent());
    }

    @Test
    @DisplayName("Should return task by ID")
    void testTaskById() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task to Find");
        task.setDescription("description");
        task.setTask_type(TaskType.LOW);
        task.setPriority("LOW");
        task.setProject(testProject);
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task to Find"));
    }
}
