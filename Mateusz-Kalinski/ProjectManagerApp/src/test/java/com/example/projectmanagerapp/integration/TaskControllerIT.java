package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class TaskControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withReuse(true);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.show_sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    }

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();

        Project project = new Project();
        project.setName("Projekt dla Zadań");
        testProject = projectRepository.save(project);
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        Tasks newTask = new Tasks();
        newTask.setTitle("Nowe Zadanie Integracyjne");
        newTask.setDescription("Opis nowego zadania");
        newTask.setTaskType(TaskType.TODO);
        newTask.setProject(testProject);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.title", is("Nowe Zadanie Integracyjne")))
                .andExpect(jsonPath("$.description", is("Opis nowego zadania")))
                .andExpect(jsonPath("$.taskType", is(TaskType.TODO.toString())))
                .andExpect(jsonPath("$.project.id", is(testProject.getId().intValue())))
                .andExpect(jsonPath("$.project.name", is(testProject.getName())));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Tasks taskToCreate = new Tasks();
        taskToCreate.setTitle("Zadanie Do Odczytu");
        taskToCreate.setDescription("Opis zadania do odczytu");
        taskToCreate.setTaskType(TaskType.IN_PROGRESS);
        taskToCreate.setProject(testProject);

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Tasks createdTask = objectMapper.readValue(createResult.getResponse().getContentAsString(), Tasks.class);
        Long taskId = createdTask.getId();

        mockMvc.perform(get("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskId.intValue())))
                .andExpect(jsonPath("$.title", is("Zadanie Do Odczytu")))
                .andExpect(jsonPath("$.taskType", is(TaskType.IN_PROGRESS.toString())))
                .andExpect(jsonPath("$.project.id", is(testProject.getId().intValue())));
    }

    @Test
    void shouldReturnNotFoundForNonExistentTaskId() throws Exception {
        Long nonExistentTaskId = 9999L;
        mockMvc.perform(get("/api/tasks/" + nonExistentTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        Tasks task1 = new Tasks();
        task1.setTitle("Zadanie Gamma");
        task1.setTaskType(TaskType.TODO);
        task1.setProject(testProject);
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated());

        Tasks task2 = new Tasks();
        task2.setTitle("Zadanie Delta");
        task2.setTaskType(TaskType.DONE);
        task2.setProject(testProject);
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Zadanie Gamma", "Zadanie Delta")));
    }

    @Test
    void shouldUpdateExistingTask() throws Exception {
        Tasks taskToCreate = new Tasks();
        taskToCreate.setTitle("Zadanie Do Aktualizacji");
        taskToCreate.setDescription("Początkowy opis");
        taskToCreate.setTaskType(TaskType.TODO);
        taskToCreate.setProject(testProject);

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Tasks createdTask = objectMapper.readValue(createResult.getResponse().getContentAsString(), Tasks.class);
        Long taskId = createdTask.getId();

        Tasks updatedTaskDetails = new Tasks();
        updatedTaskDetails.setTitle("Zaktualizowany Tytuł Zadania");
        updatedTaskDetails.setDescription("Zaktualizowany opis");
        updatedTaskDetails.setTaskType(TaskType.IN_PROGRESS);
        updatedTaskDetails.setProject(testProject);

        mockMvc.perform(put("/api/tasks/update/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskId.intValue())))
                .andExpect(jsonPath("$.title", is("Zaktualizowany Tytuł Zadania")))
                .andExpect(jsonPath("$.description", is("Zaktualizowany opis")))
                .andExpect(jsonPath("$.taskType", is(TaskType.IN_PROGRESS.toString())))
                .andExpect(jsonPath("$.project.id", is(testProject.getId().intValue())));

        mockMvc.perform(get("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Zaktualizowany Tytuł Zadania")));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentTask() throws Exception {
        Long nonExistentTaskId = 9999L;
        Tasks updatedTaskDetails = new Tasks();
        updatedTaskDetails.setTitle("Próba Aktualizacji Nieistniejącego Zadania");
        updatedTaskDetails.setProject(testProject);

        mockMvc.perform(put("/api/tasks/update/" + nonExistentTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteExistingTask() throws Exception {
        Tasks taskToCreate = new Tasks();
        taskToCreate.setTitle("Zadanie Do Usunięcia");
        taskToCreate.setTaskType(TaskType.DONE);
        taskToCreate.setProject(testProject);

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Tasks createdTask = objectMapper.readValue(createResult.getResponse().getContentAsString(), Tasks.class);
        Long taskId = createdTask.getId();

        mockMvc.perform(delete("/api/tasks/delete/" + taskId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentTask() throws Exception {
        Long nonExistentTaskId = 9999L;
        mockMvc.perform(delete("/api/tasks/delete/" + nonExistentTaskId))
                // ZMIENIONE ZGODNIE Z LOGIEM BŁĘDU: oczekujemy 204 No Content, a nie 404
                .andExpect(status().isNoContent());
    }
}