package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.repository.projects_repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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


public class projects_controller_integration_test extends integration_test_base {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private projects_repository projectsRepository;

    @AfterEach
    void cleanup() {
        projectsRepository.deleteAll();
    }

    @Test
    @DisplayName("Create Project - Integration Test")
    void test_create_project() throws Exception {
        projects project = new projects();
        project.setName("Test Project");

        ResultActions response = mockMvc.perform(post("/api/projects/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(project.getName())));
    }

    @Test
    @DisplayName("Get All Projects - Integration Test")
    void test_get_all_projects() throws Exception {
        projects project1 = new projects();
        project1.setName("Project 1");
        projectsRepository.save(project1);

        projects project2 = new projects();
        project2.setName("Project 2");
        projectsRepository.save(project2);

        ResultActions response = mockMvc.perform(get("/api/projects"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Project 1")))
                .andExpect(jsonPath("$[1].name", is("Project 2")));
    }

    @Test
    @DisplayName("Get Project By ID - Integration Test")
    void test_get_project_by_id() throws Exception {
        projects project = new projects();
        project.setName("Get Test Project");
        projects savedProject = projectsRepository.save(project);

        ResultActions response = mockMvc.perform(get("/api/projects/{id}", savedProject.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(project.getName())));
    }

    @Test
    @DisplayName("Update Project - Integration Test")
    void test_update_project() throws Exception {

        projects project = new projects();
        project.setName("Old Project Name");
        projects savedProject = projectsRepository.save(project);

        projects updatedProject = new projects();
        updatedProject.setName("New Project Name");

        ResultActions response = mockMvc.perform(put("/api/projects/{id}", savedProject.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProject)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Project Name")));
    }

    @Test
    @DisplayName("Delete Project - Integration Test")
    void test_delete_project() throws Exception {
        projects project = new projects();
        project.setName("Delete Test Project");
        projects savedProject = projectsRepository.save(project);

        ResultActions response = mockMvc.perform(delete("/api/projects/{id}", savedProject.getId()));

        response.andExpect(status().isNoContent());

        mockMvc.perform(get("/api/projects/{id}", savedProject.getId()))
                .andExpect(status().isNotFound());
    }
}