package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.service.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TasksControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    TasksControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    @Test
    @DisplayName("getTaskById")
    void getTaskById() throws  Exception{
        Tasks tasks = new Tasks();
        tasks.setTitle("Task 1");
        tasks.setDescription("This is the first task");
        tasks.setTask_type(Priority.HIGH);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tasks)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(get("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
    @Test
    @DisplayName("createTask")
    void createTask() throws Exception{
        Tasks tasks = new Tasks();
        tasks.setTitle("Task 1");
        tasks.setDescription("This is the first task");
        tasks.setTask_type(Priority.HIGH);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tasks)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
    @Test
    @DisplayName("updateTask")
    void updateTask() throws Exception{
        Tasks tasks = new Tasks();
        tasks.setTitle("Task 1");
        tasks.setDescription("This is the first task");
        tasks.setTask_type(Priority.HIGH);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tasks)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        Tasks tasks2 = new Tasks();
        tasks2.setTitle("Task 2");
        tasks2.setDescription("This is the second task");
        tasks2.setTask_type(Priority.LOW);

        mockMvc.perform(put("/api/tasks/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tasks2)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 2")))
                .andExpect(jsonPath("$.description", is("This is the second task")))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }
    @Test
    @DisplayName("deleteTask")
    void deleteTask() throws Exception{
        Tasks tasks = new Tasks();
        tasks.setTitle("Task 1");
        tasks.setDescription("This is the first task");
        tasks.setTask_type(Priority.HIGH);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tasks)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("This is the first task")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(delete("/api/tasks/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(get("/api/tasks/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}
