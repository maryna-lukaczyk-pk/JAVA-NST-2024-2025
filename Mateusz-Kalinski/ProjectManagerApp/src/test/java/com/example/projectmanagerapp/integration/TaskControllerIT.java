package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.TaskType; // Import dla TaskType
import com.example.projectmanagerapp.repository.ProjectRepository; // Będziemy potrzebować do tworzenia projektów
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
            .withDatabaseName("testdb") // Ta sama nazwa bazy co w innych IT
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
    private ProjectRepository projectRepository; // Potrzebne do setupu zadań, które należą do projektów

    private Project testProject; // Przechowamy tu projekt testowy

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
        // Czyścimy repozytoria przed każdym testem.
        // Ważne: Taski są powiązane z Projektami. Jeśli usuwasz projekty, zadania też mogą być usunięte kaskadowo.
        // Tutaj czyścimy zadania, a potem projekty, aby uniknąć problemów z foreign key constraints.
        taskRepository.deleteAll();
        projectRepository.deleteAll(); // Czyścimy też projekty, bo będziemy tworzyć projekt dla zadań

        // Tworzymy projekt, do którego będą przypisywane zadania w testach
        // Robimy to tutaj, a nie przez API, aby uprościć setup dla testów zadań.
        // Alternatywnie, każdy test zadania mógłby najpierw tworzyć projekt przez API.
        Project project = new Project();
        project.setName("Projekt dla Zadań");
        testProject = projectRepository.save(project); // Zapisujemy i przechowujemy
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        // 1. Przygotuj obiekt zadania do wysłania
        Tasks newTask = new Tasks();
        newTask.setTitle("Nowe Zadanie Integracyjne");
        newTask.setDescription("Opis nowego zadania");
        newTask.setTaskType(TaskType.TODO); // Użyj enuma TaskType
        newTask.setProject(testProject); // Przypisz istniejący projekt (utworzony w @BeforeEach)

        // 2. Wykonaj żądanie POST do /api/tasks
        // TaskController.createTask zwraca ResponseEntity<Tasks> z HttpStatus.CREATED
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated()) // Oczekujemy 201 Created
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.title", is("Nowe Zadanie Integracyjne")))
                .andExpect(jsonPath("$.description", is("Opis nowego zadania")))
                .andExpect(jsonPath("$.taskType", is(TaskType.TODO.toString())))
                // Sprawdzenie, czy projekt został poprawnie przypisany
                .andExpect(jsonPath("$.project.id", is(testProject.getId().intValue())))
                .andExpect(jsonPath("$.project.name", is(testProject.getName())));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        // 1. Najpierw utwórz zadanie, które będziemy odczytywać
        Tasks taskToCreate = new Tasks();
        taskToCreate.setTitle("Zadanie Do Odczytu");
        taskToCreate.setDescription("Opis zadania do odczytu");
        taskToCreate.setTaskType(TaskType.IN_PROGRESS);
        taskToCreate.setProject(testProject); // Używamy projektu utworzonego w @BeforeEach

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Tasks createdTask = objectMapper.readValue(createResult.getResponse().getContentAsString(), Tasks.class);
        Long taskId = createdTask.getId();

        // 2. Wykonaj żądanie GET, aby pobrać zadanie po ID
        mockMvc.perform(get("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // TaskController.getTaskById zwraca ResponseEntity.ok
                .andExpect(jsonPath("$.id", is(taskId.intValue())))
                .andExpect(jsonPath("$.title", is("Zadanie Do Odczytu")))
                .andExpect(jsonPath("$.taskType", is(TaskType.IN_PROGRESS.toString())))
                .andExpect(jsonPath("$.project.id", is(testProject.getId().intValue())));
    }

    @Test
    void shouldReturnNotFoundForNonExistentTaskId() throws Exception {
        Long nonExistentTaskId = 9999L;
        // Twój TaskController.getTaskById zwraca ResponseEntity.notFound().build()
        mockMvc.perform(get("/api/tasks/" + nonExistentTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Oczekujemy 404 Not Found
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        // 1. Utwórz kilka zadań (wszystkie będą przypisane do testProject)
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

        // 2. Wykonaj żądanie GET, aby pobrać wszystkie zadania
        // Endpoint /api/tasks w Twoim TaskController pobiera WSZYSTKIE zadania globalnie
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Dzięki @BeforeEach z taskRepository.deleteAll(), powinniśmy mieć dokładnie 2 zadania
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Zadanie Gamma", "Zadanie Delta")));
    }

    @Test
    void shouldUpdateExistingTask() throws Exception {
        // 1. Najpierw utwórz zadanie, które będziemy aktualizować
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

        // 2. Przygotuj zaktualizowane dane zadania
        Tasks updatedTaskDetails = new Tasks();
        updatedTaskDetails.setTitle("Zaktualizowany Tytuł Zadania");
        updatedTaskDetails.setDescription("Zaktualizowany opis");
        updatedTaskDetails.setTaskType(TaskType.IN_PROGRESS);
        updatedTaskDetails.setProject(testProject); // Ważne: Przekaż projekt, jeśli nie chcesz, aby został usunięty/zmieniony na null

        // 3. Wykonaj żądanie PUT, aby zaktualizować zadanie
        // TaskController.updateTask zwraca ResponseEntity.ok lub notFound
        mockMvc.perform(put("/api/tasks/update/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskId.intValue())))
                .andExpect(jsonPath("$.title", is("Zaktualizowany Tytuł Zadania")))
                .andExpect(jsonPath("$.description", is("Zaktualizowany opis")))
                .andExpect(jsonPath("$.taskType", is(TaskType.IN_PROGRESS.toString())))
                .andExpect(jsonPath("$.project.id", is(testProject.getId().intValue()))); // Sprawdź, czy projekt pozostał ten sam

        // 4. Opcjonalna weryfikacja: Pobierz zadanie i sprawdź, czy zmiany zostały trwale zapisane
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
        updatedTaskDetails.setProject(testProject); // Projekt jest wymagany przez niektóre walidacje lub logikę zapisu

        // Twój TaskController.updateTask zwraca ResponseEntity.notFound() gdy zadanie nie istnieje
        mockMvc.perform(put("/api/tasks/update/" + nonExistentTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTaskDetails)))
                .andExpect(status().isNotFound()); // Oczekujemy 404 Not Found
    }

    @Test
    void shouldDeleteExistingTask() throws Exception {
        // 1. Najpierw utwórz zadanie, które będziemy usuwać
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

        // 2. Wykonaj żądanie DELETE, aby usunąć zadanie
        // Twój TaskController.deleteTask zwraca ResponseEntity.noContent().build() po udanym usunięciu
        mockMvc.perform(delete("/api/tasks/delete/" + taskId))
                .andExpect(status().isNoContent()); // Oczekujemy 204 No Content

        // 3. Weryfikacja: Spróbuj pobrać usunięte zadanie - powinien zwrócić 404 Not Found
        mockMvc.perform(get("/api/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentTask() throws Exception {
        Long nonExistentTaskId = 9999L;

        // Twój TaskController.deleteTask ma blok try-catch, który powinien zwrócić notFound(),
        // jeśli taskService.deleteTask rzuci wyjątek (np. EmptyResultDataAccessException
        // z repozytorium, gdy zadanie nie istnieje).
        mockMvc.perform(delete("/api/tasks/delete/" + nonExistentTaskId))
                .andExpect(status().isNotFound()); // Oczekujemy 404 Not Found
    }
}