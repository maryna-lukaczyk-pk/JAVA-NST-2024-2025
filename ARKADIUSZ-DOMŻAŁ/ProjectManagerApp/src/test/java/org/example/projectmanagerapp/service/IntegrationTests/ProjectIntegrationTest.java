package org.example.projectmanagerapp.service.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();

        Projects p1 = new Projects();
        p1.setName("Projekt_test_1");

        Projects p2 = new Projects();
        p2.setName("Projekt_test_2");

        projectRepository.saveAll(List.of(p1, p2));
    }

    @AfterEach
    void cleanUp() {
        projectRepository.deleteAll();
    }

    //testuje metode get all projects
    @Test
    public void shouldReturnAllProjects() throws Exception {
        mockMvc.perform(get("/api/projects/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Projekt_test_1")))
                .andExpect(jsonPath("$[1].name", is("Projekt_test_2")));
    }

    //teraz testuje metode, która zwraca projekt po id
    @Test
    public void shouldReturnProjectById() throws Exception {
        Projects project = new Projects();
        project.setName("Projekt_test_3");
        project = projectRepository.save(project);

        mockMvc.perform(get("/api/projects/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Project found: Projekt_test_3"));
    }

    //nastepnie test metody tworzącej nowy projekt
    @Test
    public void shouldCreateNewProject() throws Exception {
        Projects newProject = new Projects();
        newProject.setName("Projekt_test_4");

        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Projekt_test_4")));
    }

    //test update'ujacy istniejący projekt
    @Test
    public void shouldUpdateExistingProject() throws Exception {
        Projects project = new Projects();
        project.setName("Old_project");
        project = projectRepository.save(project);

        Projects updatedProject = new Projects();
        updatedProject.setName("Updated_project");

        mockMvc.perform(put("/api/projects/update/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(content().string("Project updated succesfully"));
    }

    //test usuwający projekt
    @Test
    public void shouldDeleteProject() throws Exception {
        Projects project = new Projects();
        project.setName("Projekt_Delete_test");
        project = projectRepository.save(project);

        mockMvc.perform(delete("/api/projects/delete/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Project deleted succesfully"));
    }

    //teraz przypadki not-found

    @Test
    public void shouldReturnNotFoundWhenGettingNonexistentProject() throws Exception {
        mockMvc.perform(get("/api/projects/7861"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project with id: 7861 not found"));
    }

    //przy update
    @Test
    public void shouldReturnNotFoundWhenUpdatingNonexistentProject() throws Exception {
        Projects updatedProject = new Projects();
        updatedProject.setName("Update_test_notfound");

        mockMvc.perform(put("/api/projects/update/7861")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project with id: 7861 not found"));
    }

    //przy delete
    @Test
    public void shouldReturnNotFoundWhenDeletingNonexistentProject() throws Exception {
        mockMvc.perform(delete("/api/projects/delete/7861"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project not found"));
    }

}