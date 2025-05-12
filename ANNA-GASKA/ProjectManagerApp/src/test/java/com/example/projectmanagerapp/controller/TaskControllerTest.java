package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean TaskService taskService;

    @Test
    void getAllTasks() throws Exception {
        Task t = new Task(); t.setId(1L); t.setTitle("T1");
        Mockito.when(taskService.getAllTasks()).thenReturn(List.of(t));

        mvc.perform(get("/api/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("T1"));
    }

    @Test
    void createTask() throws Exception {
        Task t = new Task(); t.setTitle("A");
        Mockito.when(taskService.createTask(any())).thenReturn(t);

        mvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(t)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("A"));
    }

    @Test
    void updateTask() throws Exception {
        Task t = new Task(); t.setTitle("B");
        Mockito.when(taskService.updateTask(Mockito.eq(5L), any())).thenReturn(t);

        mvc.perform(put("/api/task/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("B"));
    }

    @Test
    void deleteTask() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(8L);

        mvc.perform(delete("/api/task/8"))
                .andExpect(status().isNoContent());
    }
}
