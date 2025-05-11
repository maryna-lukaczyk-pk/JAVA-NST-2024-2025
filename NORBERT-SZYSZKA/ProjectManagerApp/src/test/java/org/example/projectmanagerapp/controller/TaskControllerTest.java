package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    @Mock private TaskService taskService;
    @InjectMocks private TaskController controller;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllTasks() throws Exception {
        Task t1 = new Task(); t1.setId(1L); t1.setTitle("T1");
        Task t2 = new Task(); t2.setId(2L); t2.setTitle("T2");
        when(taskService.findAll()).thenReturn(Arrays.asList(t1, t2));

        mvc.perform(get("/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("T1"));
    }

    @Test
    void getTaskById_found() throws Exception {
        Task t = new Task(); t.setId(1L); t.setTitle("T1");
        when(taskService.findById(1L)).thenReturn(Optional.of(t));

        mvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("T1"));
    }

    @Test
    void getTaskById_notFound() throws Exception {
        when(taskService.findById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask() throws Exception {
        Task t = new Task(); t.setId(1L); t.setTitle("New");
        when(taskService.create(any(Task.class))).thenReturn(t);

        mvc.perform(post("/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateTask_found() throws Exception {
        Task existing = new Task(); existing.setId(1L);
        when(taskService.findById(1L)).thenReturn(Optional.of(existing));
        Task updated = new Task(); updated.setId(1L); updated.setTitle("Upd");
        when(taskService.update(eq(1L), any(Task.class))).thenReturn(updated);

        mvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Upd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Upd"));
    }

    @Test
    void deleteTask() throws Exception {
        mvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
        verify(taskService).delete(1L);
    }
}
