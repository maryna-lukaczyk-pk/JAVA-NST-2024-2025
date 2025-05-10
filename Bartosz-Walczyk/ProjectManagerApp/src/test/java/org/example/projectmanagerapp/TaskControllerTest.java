package org.example.projectmanagerapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.controller.TaskController;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    @DisplayName("GET /api/tasks should return list of tasks")
    void testGetTasks() throws Exception {
        Task t1 = new Task(); t1.setId(1L); t1.setTitle("Task1");
        Task t2 = new Task(); t2.setId(2L); t2.setTitle("Task2");
        List<Task> tasks = Arrays.asList(t1, t2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tasks)));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("GET /api/tasks/{id} should return a task when found")
    void testGetTaskByIdFound() throws Exception {
        Task t = new Task(); t.setId(5L); t.setTitle("Found");
        when(taskService.getTaskById(5L)).thenReturn(t);

        mockMvc.perform(get("/api/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(t)));

        verify(taskService).getTaskById(5L);
    }

    @Test
    @DisplayName("POST /api/tasks should create and return new task")
    void testCreateTask() throws Exception {
        Task input = new Task(); input.setTitle("NewTask");
        Task saved = new Task(); saved.setId(10L); saved.setTitle("NewTask");
        when(taskService.createTask(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(saved)));

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    @DisplayName("PUT /api/tasks should update and return the task")
    void testUpdateTask() throws Exception {
        Task update = new Task(); update.setId(15L); update.setTitle("Updated");
        when(taskService.updateTask(any(Task.class))).thenReturn(update);

        mockMvc.perform(put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(update)));

        verify(taskService).updateTask(any(Task.class));
    }

    @Test
    @DisplayName("DELETE /api/tasks should delete and return the task")
    void testDeleteTask() throws Exception {
        Task deleted = new Task(); deleted.setId(20L); deleted.setTitle("ToDelete");
        when(taskService.deleteTask(eq(20L))).thenReturn(deleted);

        mockMvc.perform(delete("/api/tasks")
                        .param("id", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(deleted)));

        verify(taskService).deleteTask(20L);
    }

    @Test
    @DisplayName("DELETE /api/tasks should handle null return")
    void testDeleteTaskNotFound() throws Exception {
        when(taskService.deleteTask(eq(99L))).thenReturn(null);

        mockMvc.perform(delete("/api/tasks")
                        .param("id", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(taskService).deleteTask(99L);
    }
}
