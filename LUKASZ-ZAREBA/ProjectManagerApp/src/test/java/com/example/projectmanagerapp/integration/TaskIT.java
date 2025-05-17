package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.task_type;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testy integracyjne dla endpointów /tasks.
 * Sprawdzają operacje CRUD (GET, POST, PUT, DELETE) na encji Task.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testUtworzenieNowegoZadania() throws Exception {
        // Utworzenie projektu jako zależności
        Project project = new Project();
        project.setName("Projekt dla zadania");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        // Tworzenie zadania z referencją do projektu
        Task task = new Task();
        task.setTitle("Nowe zadanie");
        task.setDescription("Opis zadania");
        task.setTask_type(task_type.low);
        Project refProject = new Project();
        refProject.setId(createdProject.getId());
        task.setProject(refProject);
        String taskJson = objectMapper.writeValueAsString(task);

        // Test POST /tasks - tworzenie zadania
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Nowe zadanie"))
                .andExpect(jsonPath("$.description").value("Opis zadania"))
                .andExpect(jsonPath("$.task_type").value("low"));
    }

    @Test
    public void testPobranieWszystkichZadan() throws Exception {
        // Utworzenie projektu dla zadań
        Project project = new Project();
        project.setName("ProjektZadania");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        // Dodanie dwóch zadań
        Task task1 = new Task();
        task1.setTitle("Zadanie1");
        task1.setDescription("Opis1");
        task1.setTask_type(task_type.medium);
        Project ref1 = new Project();
        ref1.setId(createdProject.getId());
        task1.setProject(ref1);
        String taskJson1 = objectMapper.writeValueAsString(task1);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson1))
                .andExpect(status().isOk());

        Task task2 = new Task();
        task2.setTitle("Zadanie2");
        task2.setDescription("Opis2");
        task2.setTask_type(task_type.high);
        Project ref2 = new Project();
        ref2.setId(createdProject.getId());
        task2.setProject(ref2);
        String taskJson2 = objectMapper.writeValueAsString(task2);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson2))
                .andExpect(status().isOk());

        // Test GET /tasks - pobranie wszystkich zadań
        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    public void testPobranieZadaniaPoId() throws Exception {
        // Utworzenie projektu
        Project project = new Project();
        project.setName("ProjektZadania2");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        // Utworzenie zadania
        Task task = new Task();
        task.setTitle("TaskPoId");
        task.setDescription("OpisId");
        task.setTask_type(task_type.low);
        Project ref = new Project();
        ref.setId(createdProject.getId());
        task.setProject(ref);
        String taskJson = objectMapper.writeValueAsString(task);
        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Task createdTask = objectMapper.readValue(response, Task.class);

        // Test GET /tasks/{id} - pobranie zadania po ID
        mockMvc.perform(get("/tasks/{id}", createdTask.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void testAktualizacjaZadania() throws Exception {
        // Utworzenie projektu
        Project project = new Project();
        project.setName("ProjektZadania3");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        // Utworzenie zadania
        Task task = new Task();
        task.setTitle("ZadanieStare");
        task.setDescription("OpisStary");
        task.setTask_type(task_type.low);
        Project ref = new Project();
        ref.setId(createdProject.getId());
        task.setProject(ref);
        String taskJson = objectMapper.writeValueAsString(task);
        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Task createdTask = objectMapper.readValue(response, Task.class);

        // Aktualizacja zadania
        Task updated = new Task();
        updated.setTitle("ZadanieNowe");
        updated.setDescription("OpisNowy");
        updated.setTask_type(task_type.medium);
        Project refUpdated = new Project();
        refUpdated.setId(createdProject.getId());
        updated.setProject(refUpdated);
        String updateJson = objectMapper.writeValueAsString(updated);
        mockMvc.perform(put("/tasks/{id}", createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andDo(print()) // Dodanie wydruku odpowiedzi
                .andExpect(status().isOk());

    }

    @Test
    public void testUsuwanieZadania() throws Exception {
        // Utworzenie projektu
        Project project = new Project();
        project.setName("ProjektZadania4");
        String projectJson = objectMapper.writeValueAsString(project);
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(projectResponse, Project.class);

        // Utworzenie zadania do usunięcia
        Task task = new Task();
        task.setTitle("ZadanieDoUsuniecia");
        task.setDescription("OpisDoUsuniecia");
        task.setTask_type(task_type.low);
        Project ref = new Project();
        ref.setId(createdProject.getId());
        task.setProject(ref);
        String taskJson = objectMapper.writeValueAsString(task);
        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Task createdTask = objectMapper.readValue(response, Task.class);

        // Test DELETE /tasks/{id} - usuwanie zadania
        mockMvc.perform(delete("/tasks/{id}", createdTask.getId()))
                .andExpect(status().isNoContent());
    }
}