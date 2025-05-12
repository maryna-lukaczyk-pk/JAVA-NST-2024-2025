package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Projects;
import com.example.projectmanagerapp.service.ProjectService;
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

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean ProjectService projectService;

    @Test
    void getAllProjects() throws Exception {
        Projects p = new Projects(); p.setId(1L); p.setName("X");
        Mockito.when(projectService.getAllProjects()).thenReturn(List.of(p));

        mvc.perform(get("/api/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("X"));
    }

    @Test
    void createProject() throws Exception {
        Projects p = new Projects(); p.setName("New");
        Mockito.when(projectService.createProject(any())).thenReturn(p);

        mvc.perform(post("/api/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void updateProject() throws Exception {
        Projects p = new Projects(); p.setName("Upd");
        Mockito.when(projectService.updateProject(Mockito.eq(2L), any())).thenReturn(p);

        mvc.perform(put("/api/project/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Upd"));
    }

    @Test
    void deleteProject() throws Exception {
        Mockito.doNothing().when(projectService).deleteProject(3L);

        mvc.perform(delete("/api/project/3"))
                .andExpect(status().isNoContent());
    }
}