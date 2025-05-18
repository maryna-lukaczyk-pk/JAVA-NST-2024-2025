package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository; // Dodaj ten import
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles; // Odkomentuj jeśli używasz
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
// @ActiveProfiles("test") // Opcjonalnie, jeśli masz profil 'test' w application.properties
public class ProjectControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired // Wstrzyknij ProjectRepository, aby móc czyścić dane
    private ProjectRepository projectRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.show_sql", () -> "true"); // Opcjonalnie: pokaż SQL w logach
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true"); // Opcjonalnie: formatuj SQL
    }

    @BeforeEach
    void setUp() {
        // Czyść tabelę projektów przed każdym testem, aby zapewnić izolację danych
        // Jeśli masz relacje kaskadowe lub inne encje, które powinny być czyszczone,
        // dodaj tutaj również projectRepository.deleteAllInBatch() lub czyszczenie innych repozytoriów.
        // Dla Project, który ma relację ManyToMany z Users, może być konieczne ostrożniejsze czyszczenie
        // lub zapewnienie, że dane testowe nie kolidują. Na razie deleteAll() powinno być OK dla Project.
        projectRepository.deleteAll();
    }

    @Test
    void shouldCreateNewProject() throws Exception {
        Project newProject = new Project();
        newProject.setName("Nowy Projekt Integracyjny");

        MvcResult mvcResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is("Nowy Projekt Integracyjny")))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Project createdProject = objectMapper.readValue(responseBody, Project.class);

        mockMvc.perform(get("/api/projects/" + createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nowy Projekt Integracyjny")));
    }

    @Test
    void shouldGetProjectById() throws Exception {
        Project projectToCreate = new Project();
        projectToCreate.setName("Projekt Do Odczytu");

        MvcResult createResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(createResult.getResponse().getContentAsString(), Project.class);
        Long projectId = createdProject.getId();

        mockMvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue()))) // Użyj .intValue() lub .longValue() w zależności od preferencji
                .andExpect(jsonPath("$.name", is("Projekt Do Odczytu")));
    }

    @Test
    void shouldReturnNotFoundForNonExistentProjectId() throws Exception {
        Long nonExistentProjectId = 9999L;
        mockMvc.perform(get("/api/projects/" + nonExistentProjectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllProjects() throws Exception {
        Project project1 = new Project();
        project1.setName("Projekt Alpha");
        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project1)))
                .andExpect(status().isCreated());

        Project project2 = new Project();
        project2.setName("Projekt Beta");
        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/projects/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Dzięki @BeforeEach z projectRepository.deleteAll(), powinniśmy mieć dokładnie 2 projekty
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Projekt Alpha"))) // Zakładając kolejność wstawiania lub sortowanie
                .andExpect(jsonPath("$[1].name", is("Projekt Beta")))  // Można też użyć bardziej elastycznych matcherów jak poniżej
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Projekt Alpha", "Projekt Beta"))); // Sprawdza nazwy niezależnie od kolejności
    }

    @Test
    void shouldUpdateExistingProject() throws Exception {
        // 1. Najpierw utwórz projekt, który będziemy aktualizować
        Project projectToCreate = new Project();
        projectToCreate.setName("Projekt Do Aktualizacji");

        MvcResult createResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(createResult.getResponse().getContentAsString(), Project.class);
        Long projectId = createdProject.getId();

        // 2. Przygotuj zaktualizowane dane projektu
        Project updatedProjectDetails = new Project();
        // Ważne: W Twoim ProjectController metoda update przyjmuje obiekt Project.
        // Serwis ProjectService robi: projectDetails.setId(id);
        // więc nie musimy ustawiać ID w obiekcie wysyłanym w żądaniu PUT,
        // ale nazwa musi być inna, aby sprawdzić aktualizację.
        updatedProjectDetails.setName("Zaktualizowana Nazwa Projektu");

        // 3. Wykonaj żądanie PUT, aby zaktualizować projekt
        mockMvc.perform(put("/api/projects/update/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectDetails)))
                .andExpect(status().isOk()) // Oczekujemy 200 OK
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.name", is("Zaktualizowana Nazwa Projektu")));

        // 4. Opcjonalna weryfikacja: Pobierz projekt i sprawdź, czy zmiany zostały trwale zapisane
        mockMvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Zaktualizowana Nazwa Projektu")));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentProject() throws Exception {
        Long nonExistentProjectId = 9999L;
        Project updatedProjectDetails = new Project();
        updatedProjectDetails.setName("Próba Aktualizacji Nieistniejącego");

        mockMvc.perform(put("/api/projects/update/" + nonExistentProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectDetails)))
                .andExpect(status().isNotFound()); // Oczekujemy 404 Not Found
    }

    @Test
    void shouldDeleteExistingProject() throws Exception {
        // 1. Najpierw utwórz projekt, który będziemy usuwać
        Project projectToCreate = new Project();
        projectToCreate.setName("Projekt Do Usunięcia");

        MvcResult createResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(createResult.getResponse().getContentAsString(), Project.class);
        Long projectId = createdProject.getId();

        // 2. Wykonaj żądanie DELETE, aby usunąć projekt
        mockMvc.perform(delete("/api/projects/delete/" + projectId))
                .andExpect(status().isNoContent()); // Oczekujemy 204 No Content

        // 3. Weryfikacja: Spróbuj pobrać usunięty projekt - powinien zwrócić 404 Not Found
        mockMvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentProject() throws Exception {
        Long nonExistentProjectId = 9999L;

        mockMvc.perform(delete("/api/projects/delete/" + nonExistentProjectId))
                .andExpect(status().isNotFound()); // Oczekujemy 404 Not Found
        // (zgodnie z Twoją implementacją ProjectController/Service,
        //  która rzuca wyjątek, a Spring Boot mapuje go na 404)
    }

    @Test
    void shouldAssignUserToProject() throws Exception {
        // (a) Utwórz użytkownika i projekt za pomocą API.

        // Tworzenie użytkownika
        Users userToAssign = new Users();
        userToAssign.setUsername("userDoPrzypisania");
        MvcResult userCreateResult = mockMvc.perform(post("/api/users/create") // Używamy endpointu z UserController
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToAssign)))
                .andExpect(status().isOk()) // Zakładając, że createUser zwraca 200 OK
                .andReturn();
        Users createdUser = objectMapper.readValue(userCreateResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();

        // Tworzenie projektu
        Project projectForAssignment = new Project();
        projectForAssignment.setName("Projekt Do Przypisania Użytkownika");
        MvcResult projectCreateResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectForAssignment)))
                .andExpect(status().isCreated())
                .andReturn();
        Project createdProject = objectMapper.readValue(projectCreateResult.getResponse().getContentAsString(), Project.class);
        Long projectId = createdProject.getId();

        // (b) Przypisz użytkownika do projektu przez endpoint /api/projects/{projectId}/users/{userId}
        MvcResult assignmentResult = mockMvc.perform(post("/api/projects/" + projectId + "/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                // (c) Sprawdź status odpowiedzi (200 OK).
                .andExpect(status().isOk())
                // (d) Zweryfikuj obecność użytkownika w liście członków projektu.
                // Sprawdzamy, czy zwrócony projekt w odpowiedzi zawiera przypisanego użytkownika
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.name", is("Projekt Do Przypisania Użytkownika")))
                .andExpect(jsonPath("$.users", hasSize(1))) // Oczekujemy jednego użytkownika w liście
                .andExpect(jsonPath("$.users[0].id", is(userId.intValue()))) // Sprawdzamy ID użytkownika
                .andExpect(jsonPath("$.users[0].username", is("userDoPrzypisania"))) // Sprawdzamy username
                .andReturn();

        // Dodatkowa, bardziej szczegółowa weryfikacja (opcjonalna)
        Project projectAfterAssignment = objectMapper.readValue(assignmentResult.getResponse().getContentAsString(), Project.class);
        assertNotNull(projectAfterAssignment.getUsers());
        assertEquals(1, projectAfterAssignment.getUsers().size());
        assertTrue(projectAfterAssignment.getUsers().stream().anyMatch(u -> u.getId().equals(userId) && u.getUsername().equals("userDoPrzypisania")));

        // Można też pobrać projekt ponownie przez GET i sprawdzić listę użytkowników,
        // aby mieć 100% pewności, że zmiana jest trwała i odczytywalna.
        mockMvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(userId.intValue())));
    }
}