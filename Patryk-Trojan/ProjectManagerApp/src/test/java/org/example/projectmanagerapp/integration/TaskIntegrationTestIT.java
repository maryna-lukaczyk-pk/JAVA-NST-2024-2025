package org.example.projectmanagerapp.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.ITTestConfiguration;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.enums.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ITTestConfiguration.class})
@AutoConfigureMockMvc
public class TaskIntegrationTestIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldDeleteTaskById() throws Exception {
        // given
        Task task = new Task();
        task.setTitle("Test Task");
        taskRepository.save(task);

        // when
        mockMvc.perform(delete("/api/task/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        List<Task> all = taskRepository.findAll();
        assertThat(all.isEmpty()).isTrue();
    }

    @Test
    void shouldFindTaskById() throws Exception {
        // given
        Task task = new Task();
        task.setTitle("Test Task");
        Task savedTask = taskRepository.save(task);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/task/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        Task resultTask = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Task.class);
        assertThat(resultTask).isNotNull();
        assertThat(resultTask.getTitle()).isEqualTo(task.getTitle());

        Task taskFromDb = taskRepository.findById(resultTask.getId()).orElseThrow();
        assertThat(taskFromDb.getId()).isEqualTo(savedTask.getId());
    }

    @Test
    void shouldCreateTask() throws Exception {
        // given
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Task description");
        task.setTasktype(TaskType.BUG);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/task")
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        Task resultTask = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Task.class);
        assertThat(resultTask).isNotNull();
        assertThat(resultTask.getTitle()).isEqualTo(task.getTitle());
        assertThat(resultTask.getDescription()).isEqualTo(task.getDescription());
        assertThat(resultTask.getTasktype()).isEqualTo(task.getTasktype());

        Task taskFromDb = taskRepository.findById(resultTask.getId()).orElseThrow();
        assertThat(taskFromDb.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    void shouldFindAllTasks() throws Exception {
        // given
        Task task1 = new Task();
        Task task2 = new Task();
        task1.setTitle("Task 1");
        task2.setTitle("Task 2");

        taskRepository.save(task1);
        taskRepository.save(task2);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/task")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // then
        String response = mvcResult.getResponse().getContentAsString();
        List<Task> returnedTasks = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Task.class));

        assertThat(returnedTasks.size()).isEqualTo(2);
        assertThat(returnedTasks.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(returnedTasks.get(1).getTitle()).isEqualTo("Task 2");
    }

    @Test
    void shouldUpdateTask() throws Exception {
        // given
        Task existingTask = new Task();
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setTasktype(TaskType.FEATURE); // Ustawiamy początkowy typ
        Task savedTask = taskRepository.save(existingTask);

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Description");
        updatedTask.setTasktype(TaskType.BUG); // Typ który chcemy ustawić

        // when
        mockMvc.perform(put("/api/task/" + savedTask.getId())
                        .content(objectMapper.writeValueAsString(updatedTask))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());

        // then
        Task updatedTaskFromDb = taskRepository.findById(savedTask.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        assertThat(updatedTaskFromDb.getTitle()).isEqualTo("New Title");
        assertThat(updatedTaskFromDb.getDescription()).isEqualTo("New Description");
        assertThat(updatedTaskFromDb.getTasktype()).isEqualTo(TaskType.BUG); // Sprawdzamy czy typ się zmienił
    }


}
