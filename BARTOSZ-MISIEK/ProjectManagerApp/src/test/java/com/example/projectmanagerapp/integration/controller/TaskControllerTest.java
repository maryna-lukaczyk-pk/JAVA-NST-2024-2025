package com.example.projectmanagerapp.integration.controller;

import com.example.projectmanagerapp.integration.utilities.AssertionHelper;
import org.example.projectmanager.dto.task.TaskDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanager.dto.task.TaskCreateDto;
import org.example.projectmanager.dto.task.TaskEditDto;
import org.example.projectmanager.entity.task.TaskType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String BASE_URL = "/api/tasks";

    private String GetIdUrl(Long id) {
        return BASE_URL + "/" + id;
    }

    @Test
    public void testTaskGetAll() throws Exception {
        TaskCreateDto dto1 = new TaskCreateDto();
        dto1.title = "Task 1";
        dto1.description = "desc1";
        dto1.taskType = TaskType.CHORE;
        Long id1 = CreateTask(dto1);

        TaskCreateDto dto2 = new TaskCreateDto();
        dto2.title = "Task 2";
        dto2.description = "desc2";
        dto2.taskType = TaskType.HOUSEWORK;
        Long id2 = CreateTask(dto2);

        var createdTask1 = getTaskDto(id1);
        var createdTask2 = getTaskDto(id2);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    TaskDto[] tasks = objectMapper.readValue(content, TaskDto[].class);
                    boolean found1 = Arrays.asList(tasks).contains(createdTask1);
                    Assertions.assertTrue(found1);

                    boolean found2 = Arrays.asList(tasks).contains(createdTask2);
                    Assertions.assertTrue(found2);
                });
    }

    private @NotNull Long CreateTask(TaskCreateDto dto1) throws Exception {
        String json = objectMapper.writeValueAsString(dto1);
        String response = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return Long.valueOf(response);
    }

    @Test
    public void testTaskCreation() throws Exception {
        TaskCreateDto dto = new TaskCreateDto();
        dto.title = "Test Task";
        dto.description = "desc";
        dto.taskType = TaskType.CHORE;
        Long id = CreateTask(dto);
        Assertions.assertTrue(id > 0);

        var task = getTaskDto(id);
        Assertions.assertEquals(id, task.id);
        Assertions.assertEquals(dto.title, task.title);
    }

    @Test
    public void testTaskNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(99999L)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTaskGetAndDelete() throws Exception {
        TaskCreateDto dto = new TaskCreateDto();
        dto.title = "integrationtask";
        dto.description = "desc";
        dto.taskType = TaskType.CHORE;
        Long id = CreateTask(dto);

        TaskDto task = getTaskDto(id);
        Assertions.assertEquals(id, task.id);

        var IdUrl = GetIdUrl(id);
        mockMvc.perform(MockMvcRequestBuilders.delete(IdUrl))
                .andExpect(status().isOk())
                .andExpect(AssertionHelper::AssertIsEmpty);

        mockMvc.perform(MockMvcRequestBuilders.get(IdUrl))
                .andExpect(status().isBadRequest());
    }

    private TaskDto getTaskDto(Long id) throws Exception {
        String content = mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(id)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, TaskDto.class);
    }

    @Test
    public void testTaskUpdate() throws Exception {
        var oldName = "toupdate";
        var newName = "updatedtask";
        var description = "desc";

        TaskCreateDto createDto = new TaskCreateDto();
        createDto.title = oldName;
        createDto.description = description;
        createDto.taskType = TaskType.CHORE;
        Long id = CreateTask(createDto);

        TaskEditDto editDto = new TaskEditDto();
        editDto.id = id;
        editDto.title = newName;
        editDto.description = createDto.description;
        editDto.taskType = createDto.taskType;

        String updateJson = objectMapper.writeValueAsString(editDto);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(AssertionHelper::AssertIsEmpty);

        var task = getTaskDto(id);
        Assertions.assertEquals(id, task.id);
        Assertions.assertEquals(editDto.title, task.title);
    }
}
