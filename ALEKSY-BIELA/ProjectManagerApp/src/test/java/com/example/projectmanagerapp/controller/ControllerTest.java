package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
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
public class ControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    ControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("getProjectById")
    void getProjectById() throws  Exception{
        Project project = new Project();
        project.setName("New Project");
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful()).andReturn();


        mockMvc.perform(get("/api/projects/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
    @Test
    @DisplayName("getAllProjects")
    void getAllProjects() throws Exception {
        Project project = new Project();
        project.setName("New Project");
        Project project2 = new Project();
        project2.setName("New Project2");
        Project project3 = new Project();
        project3.setName("New Project3");


        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project2)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("New Project2")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project3)))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Project3")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].name", is("New Project")))
                .andExpect(jsonPath("$[1].name", is("New Project2")))
                .andExpect(jsonPath("$[2].name", is("New Project3")))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    @DisplayName("createProject")
    void createProject() throws Exception{
        Project project = new Project();
        project.setName("New Project");
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
    @Test
    @DisplayName("updateProject")
    void updateProject() throws Exception{
        Project project = new Project();
        project.setName("New Project");
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        Project project2 = new Project();
        project2.setName("New Project2");

        mockMvc.perform(put("/api/projects/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project2)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project2")))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Test
    @DisplayName("deleteProject")
    void deleteProject() throws Exception{
        Project project = new Project();
        project.setName("New Project");
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(delete("/api/projects/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        mockMvc.perform(get("/api/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}
