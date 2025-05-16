package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.repository.projects_repository;
import com.example.projectmanagerapp.repository.tasks_repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class tasks_controller_integration_test extends integration_test_base {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private tasks_repository tasksRepository;

    @Autowired
    private projects_repository projectsRepository;

    private projects testProject;

    @BeforeEach
    void setup() {
        testProject = new projects();
        testProject.setName("Test Project for Tasks");
        testProject = projectsRepository.save(testProject);
    }

    @AfterEach
    void cleanup() {
        tasksRepository.deleteAll();
        projectsRepository.deleteAll();
    }

    @Test
    @DisplayName("Create Task - Integration Test")
    void test_create_task() throws Exception {
        tasks task = new tasks();
        task.setTitle("Sample Task");
        task.setDescription("Sample Description");
        task.setPriority("HIGH");
        task.setProject_id(testProject.getId());
        task.setProjects(testProject);

        tasks savedTask = tasksRepository.save(task);

        ResultActions response = mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(task.getTitle())))
                .andExpect(jsonPath("$.description", is(task.getDescription())));
    }

    @Test
    @DisplayName("Get All Tasks - Integration Test")
    void test_get_all_tasks() throws Exception {
        tasks task1 = new tasks();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setPriority("HIGH");
        task1.setProject_id(testProject.getId());
        task1.setProjects(testProject);
        tasksRepository.save(task1);

        tasks task2 = new tasks();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setPriority("MEDIUM");
        task2.setProject_id(testProject.getId());
        task2.setProjects(testProject);
        tasksRepository.save(task2);

        System.out.println("Liczba zadań w repozytorium: " + tasksRepository.count());
        for (tasks task : tasksRepository.findAll()) {
            System.out.println("Zadanie: " + task.getId() + " - " + task.getTitle());
        }

        ResultActions response = mockMvc.perform(get("/api/tasks"));
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Task By ID - Integration Test")
    void test_get_task_by_id() throws Exception {
        tasks task = new tasks();
        task.setTitle("Get Test Task");
        task.setDescription("Get Test Description");
        task.setPriority("LOW");
        task.setProject_id(testProject.getId());
        task.setProjects(testProject);
        tasks savedTask = tasksRepository.save(task);

        System.out.println("Utworzono zadanie z ID: " + savedTask.getId());

        ResultActions response = mockMvc.perform(get("/api/tasks/{id}", savedTask.getId()));
        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Task - Integration Test")
    void test_update_task() throws Exception {
        tasks task = new tasks();
        task.setTitle("Old Task Title");
        task.setDescription("Old Description");
        task.setPriority("MEDIUM");
        task.setProject_id(testProject.getId());
        task.setProjects(testProject);
        tasks savedTask = tasksRepository.save(task);

        tasks updatedTask = new tasks();
        updatedTask.setTitle("New Task Title");
        updatedTask.setDescription("New Description");
        updatedTask.setPriority("HIGH");
        updatedTask.setProject_id(testProject.getId());

        ResultActions response = mockMvc.perform(put("/api/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)));

        response.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Task - Integration Test")
    void test_delete_task() throws Exception {
        tasks task = new tasks();
        task.setTitle("Delete Test Task");
        task.setDescription("Delete Test Description");
        task.setPriority("LOW");
        task.setProject_id(testProject.getId());
        task.setProjects(testProject);
        tasks savedTask = tasksRepository.save(task);

        ResultActions response = mockMvc.perform(delete("/api/tasks/{id}", savedTask.getId()));

        response.andExpect(status().isNoContent());
    }
}