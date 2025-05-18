package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository;
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

// Dodaj ten import dla .andDo(print())
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
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

    @Autowired
    private ProjectRepository projectRepository;

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
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
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
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Projekt Alpha", "Projekt Beta")));
    }

    @Test
    void shouldUpdateExistingProject() throws Exception {
        Project projectToCreate = new Project();
        projectToCreate.setName("Projekt Do Aktualizacji");

        MvcResult createResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(createResult.getResponse().getContentAsString(), Project.class);
        Long projectId = createdProject.getId();

        Project updatedProjectDetails = new Project();
        updatedProjectDetails.setName("Zaktualizowana Nazwa Projektu");

        mockMvc.perform(put("/api/projects/update/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.name", is("Zaktualizowana Nazwa Projektu")));

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
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteExistingProject() throws Exception {
        Project projectToCreate = new Project();
        projectToCreate.setName("Projekt Do Usunięcia");

        MvcResult createResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(createResult.getResponse().getContentAsString(), Project.class);
        Long projectId = createdProject.getId();

        mockMvc.perform(delete("/api/projects/delete/" + projectId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentProject() throws Exception {
        Long nonExistentProjectId = 9999L;
        mockMvc.perform(delete("/api/projects/delete/" + nonExistentProjectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAssignUserToProject() throws Exception {
        Users userToAssign = new Users();
        userToAssign.setUsername("userDoPrzypisania");
        MvcResult userCreateResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToAssign)))
                .andExpect(status().isOk())
                .andReturn();
        Users createdUser = objectMapper.readValue(userCreateResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();
        assertNotNull(userId, "User ID should not be null after creation");

        Project projectForAssignment = new Project();
        projectForAssignment.setName("Projekt Do Przypisania Użytkownika");
        MvcResult projectCreateResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectForAssignment)))
                .andExpect(status().isCreated())
                .andReturn();
        Project createdProject = objectMapper.readValue(projectCreateResult.getResponse().getContentAsString(), Project.class);
        Long projectId = createdProject.getId();
        assertNotNull(projectId, "Project ID should not be null after creation");

        mockMvc.perform(post("/api/projects/" + projectId + "/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Sprawdzamy status
                .andDo(print()); // Drukujemy odpowiedź, żeby zobaczyć co jest nie tak z ciałem odpowiedzi na POST

        // Weryfikacja przez ponowne pobranie projektu (bardziej niezawodna niż analiza ciała odpowiedzi z POST,
        // zwłaszcza jeśli są problemy z serializacją JSON w tej odpowiedzi)
        mockMvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.name", is("Projekt Do Przypisania Użytkownika")))
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(userId.intValue())))
                .andExpect(jsonPath("$.users[0].username", is("userDoPrzypisania")));
    }
}