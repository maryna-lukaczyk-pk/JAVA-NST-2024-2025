package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.LowPriority;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task1;
    private Task task2;
    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setName("TestProject");

        task1 = new Task();
        task1.setTitle("TestTask1");
        task1.setDescription("Description for TestTask1");
        task1.setTaskTypeEnum(TaskType.BUG);
        task1.setPriority(new HighPriority());
        task1.setProject(project);

        task2 = new Task();
        task2.setTitle("TestTask2");
        task2.setDescription("Description for TestTask2");
        task2.setTaskTypeEnum(TaskType.FEATURE);
        task2.setPriority(new LowPriority());
        task2.setProject(project);
    }

    @Test
    @DisplayName("Should return all tasks")
    void getAllTasks() throws Exception {
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("TestTask1")))
                .andExpect(jsonPath("$[0].description", is("Description for TestTask1")))
                .andExpect(jsonPath("$[0].taskTypeEnum", is("BUG")))
                .andExpect(jsonPath("$[1].title", is("TestTask2")))
                .andExpect(jsonPath("$[1].description", is("Description for TestTask2")))
                .andExpect(jsonPath("$[1].taskTypeEnum", is("FEATURE")));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("Should return task by ID")
    void getTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(task1);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("TestTask1")))
                .andExpect(jsonPath("$.description", is("Description for TestTask1")))
                .andExpect(jsonPath("$.taskTypeEnum", is("BUG")));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @DisplayName("Should create a new task")
    void createTask() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(task1);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("TestTask1")))
                .andExpect(jsonPath("$.description", is("Description for TestTask1")))
                .andExpect(jsonPath("$.taskTypeEnum", is("BUG")));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    @DisplayName("Should update task")
    void updateTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle("UpdatedTask");
        updatedTask.setDescription("Updated Description");
        updatedTask.setTaskTypeEnum(TaskType.IMPROVEMENT);
        updatedTask.setPriority(new LowPriority());
        updatedTask.setProject(project);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("UpdatedTask")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.taskTypeEnum", is("IMPROVEMENT")));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @DisplayName("Should delete task")
    void deleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }
}
