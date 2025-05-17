package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class ProjectIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testUtworzenieNowegoProjektu() throws Exception {
        Project project = new Project();
        project.setName("Projekt Testowy");
        String projectJson = objectMapper.writeValueAsString(project);

        // Test POST /projects - tworzenie projektu
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Projekt Testowy"));
    }

    @Test
    public void testPobranieWszystkichProjektow() throws Exception {
        // Dodanie dwóch projektów
        Project project1 = new Project();
        project1.setName("Projekt1");
        String json1 = objectMapper.writeValueAsString(project1);
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isOk());

        Project project2 = new Project();
        project2.setName("Projekt2");
        String json2 = objectMapper.writeValueAsString(project2);
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isOk());

        // Test GET /projects
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testPobranieProjektuPoId() throws Exception {
        // Utworzenie projektu
        Project project = new Project();
        project.setName("Jeden Projekt");
        String projectJson = objectMapper.writeValueAsString(project);
        String response = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project created = objectMapper.readValue(response, Project.class);

        // Test GET /projects/{id}
        mockMvc.perform(get("/projects/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jeden Projekt"));
    }

    @Test
    public void testAktualizacjaProjektu() throws Exception {
        // Utworzenie projektu
        Project project = new Project();
        project.setName("Stary Projekt");
        String projectJson = objectMapper.writeValueAsString(project);
        String response = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project created = objectMapper.readValue(response, Project.class);

        // Aktualizacja projektu
        Project updated = new Project();
        updated.setName("Nowy Projekt");
        String updateJson = objectMapper.writeValueAsString(updated);
        mockMvc.perform(put("/projects/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nowy Projekt"));
    }

    @Test
    public void testUsuwanieProjektu() throws Exception {
        // Utworzenie projektu do usunięcia
        Project project = new Project();
        project.setName("Do Usuniecia");
        String projectJson = objectMapper.writeValueAsString(project);
        String response = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project created = objectMapper.readValue(response, Project.class);

        // Test DELETE /projects/{id}
        mockMvc.perform(delete("/projects/{id}", created.getId()))
                .andExpect(status().isNoContent());
    }
}
