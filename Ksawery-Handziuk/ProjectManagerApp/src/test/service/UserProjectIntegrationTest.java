// src/test/java/com/example/projectmanagerapp/UserProjectIntegrationTest.java
package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProjectManagerAppApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Testy integracyjne dla przepływów aplikacji")
public class UserProjectIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.4")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository; // Dodane dla testów zadań

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.main.allow-bean-definition-overriding", () -> "true"); // Może być potrzebne w niektórych konfiguracjach testowych
    }

    @BeforeEach
    @Transactional
    void setUp() {
        // Czyszczenie bazy danych przed każdym testem
        taskRepository.deleteAll(); // Najpierw encje zależne lub te, które nie mają zależności
        // Usuwanie powiązań między Project i User przed usunięciem samych encji
        projectRepository.findAll().forEach(project -> {
            project.getUsers().clear();
            projectRepository.save(project);
        });
        userRepository.findAll().forEach(user -> {
            user.getProjects().clear();
            userRepository.save(user);
        });

        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // Dodatkowe czyszczenie dla pewności
        taskRepository.deleteAll();
        projectRepository.findAll().forEach(project -> {
            project.getUsers().clear();
            projectRepository.save(project);
        });
        userRepository.findAll().forEach(user -> {
            user.getProjects().clear();
            userRepository.save(user);
        });
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    // --- Helper Methods ---
    private User createUserViaApi(String username) throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
    }

    private Project createProjectViaApi(String projectName) throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("name", projectName);
        MvcResult result = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), Project.class);
    }

    // Zakładamy, że Task ma endpoint do tworzenia lub tworzymy go bezpośrednio
    private Task createTaskDirectly(String description) {
        Task task = new Task(description);
        return taskRepository.save(task);
    }


    // --- Testy CRUD dla Użytkownika ---

    @Test
    @DisplayName("[User API] Tworzenie użytkownika - sukces")
    void shouldCreateUser_Success() throws Exception {
        User createdUser = createUserViaApi("testUserApi");
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("testUserApi");
        assertThat(userRepository.findByUsername("testUserApi")).isPresent();
    }

    @Test
    @DisplayName("[User API] Tworzenie użytkownika - pusta nazwa")
    void shouldNotCreateUser_EmptyUsername() throws Exception {
        Map<String, String> userPayload = new HashMap<>();
        userPayload.put("username", "");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Nazwa użytkownika nie może być pusta")));
    }

    @Test
    @DisplayName("[User API] Tworzenie użytkownika - nazwa już istnieje")
    void shouldNotCreateUser_UsernameExists() throws Exception {
        createUserViaApi("existingUser");
        Map<String, String> userPayload = new HashMap<>();
        userPayload.put("username", "existingUser");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPayload)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Nazwa użytkownika 'existingUser' już istnieje.")));
    }

    @Test
    @DisplayName("[User API] Pobieranie wszystkich użytkowników")
    void shouldGetAllUsers() throws Exception {
        createUserViaApi("user1");
        createUserViaApi("user2");
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[1].username", is("user2")));
    }

    @Test
    @DisplayName("[User API] Pobieranie użytkownika po ID - sukces")
    void shouldGetUserById_Success() throws Exception {
        User user = createUserViaApi("userById");
        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is("userById")));
    }

    @Test
    @DisplayName("[User API] Pobieranie użytkownika po ID - nie znaleziono")
    void shouldNotGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/users/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Nie znaleziono użytkownika o ID: 9999")));
    }

    @Test
    @DisplayName("[User API] Aktualizacja użytkownika - sukces")
    void shouldUpdateUser_Success() throws Exception {
        User user = createUserViaApi("userToUpdate");
        Map<String, String> updatedPayload = new HashMap<>();
        updatedPayload.put("username", "updatedUserApi");

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updatedUserApi")));
        assertThat(userRepository.findById(user.getId()).get().getUsername()).isEqualTo("updatedUserApi");
    }

    @Test
    @DisplayName("[User API] Aktualizacja użytkownika - nie znaleziono")
    void shouldNotUpdateUser_NotFound() throws Exception {
        Map<String, String> updatedPayload = new HashMap<>();
        updatedPayload.put("username", "updatedUserApi");
        mockMvc.perform(put("/api/users/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPayload)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[User API] Aktualizacja użytkownika - pusta nowa nazwa")
    void shouldNotUpdateUser_EmptyNewUsername() throws Exception {
        User user = createUserViaApi("userForEmptyUpdate");
        Map<String, String> updatedPayload = new HashMap<>();
        updatedPayload.put("username", "");
        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Nowa nazwa użytkownika nie może być pusta.")));
    }

    @Test
    @DisplayName("[User API] Aktualizacja użytkownika - nowa nazwa zajęta")
    void shouldNotUpdateUser_NewUsernameTaken() throws Exception {
        User user1 = createUserViaApi("userOne");
        User user2 = createUserViaApi("userTwo"); // Nazwa, na którą chcemy zmienić
        Map<String, String> updatedPayload = new HashMap<>();
        updatedPayload.put("username", "userTwo"); // Próba zmiany nazwy userOne na userTwo

        mockMvc.perform(put("/api/users/" + user1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPayload)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Nazwa użytkownika 'userTwo' jest już zajęta przez innego użytkownika.")));
    }


    @Test
    @DisplayName("[User API] Usuwanie użytkownika - sukces")
    void shouldDeleteUser_Success() throws Exception {
        User user = createUserViaApi("userToDelete");
        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    @DisplayName("[User API] Usuwanie użytkownika - nie znaleziono")
    void shouldNotDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/users/9999"))
                .andExpect(status().isNotFound());
    }

    // --- Testy CRUD dla Projektu ---

    @Test
    @DisplayName("[Project API] Tworzenie projektu - sukces")
    void shouldCreateProject_Success() throws Exception {
        Project createdProject = createProjectViaApi("Test Project API");
        assertThat(createdProject.getId()).isNotNull();
        assertThat(createdProject.getName()).isEqualTo("Test Project API");
        assertThat(projectRepository.findByName("Test Project API")).isPresent();
    }

    @Test
    @DisplayName("[Project API] Tworzenie projektu - pusta nazwa")
    void shouldNotCreateProject_EmptyName() throws Exception {
        Map<String, String> projectPayload = new HashMap<>();
        projectPayload.put("name", "");
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectPayload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Nazwa projektu nie może być pusta.")));
    }

    @Test
    @DisplayName("[Project API] Tworzenie projektu - nazwa już istnieje")
    void shouldNotCreateProject_NameExists() throws Exception {
        createProjectViaApi("Existing Project");
        Map<String, String> projectPayload = new HashMap<>();
        projectPayload.put("name", "Existing Project");
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectPayload)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Projekt o nazwie 'Existing Project' już istnieje.")));
    }

    @Test
    @DisplayName("[Project API] Pobieranie wszystkich projektów")
    void shouldGetAllProjects() throws Exception {
        createProjectViaApi("Project Alpha");
        createProjectViaApi("Project Beta");
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Project Alpha")))
                .andExpect(jsonPath("$[1].name", is("Project Beta")));
    }

    @Test
    @DisplayName("[Project API] Pobieranie projektu po ID - sukces")
    void shouldGetProjectById_Success() throws Exception {
        Project project = createProjectViaApi("ProjectById");
        mockMvc.perform(get("/api/projects/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(project.getId().intValue())))
                .andExpect(jsonPath("$.name", is("ProjectById")));
    }

    @Test
    @DisplayName("[Project API] Pobieranie projektu po ID - nie znaleziono")
    void shouldNotGetProjectById_NotFound() throws Exception {
        mockMvc.perform(get("/api/projects/9998"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Nie znaleziono projektu o ID: 9998")));
    }

    @Test
    @DisplayName("[Project API] Aktualizacja projektu - sukces")
    void shouldUpdateProject_Success() throws Exception {
        Project project = createProjectViaApi("ProjectToUpdate");
        Map<String, String> updatedPayload = new HashMap<>();
        updatedPayload.put("name", "UpdatedProjectName");

        mockMvc.perform(put("/api/projects/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedProjectName")));
        assertThat(projectRepository.findById(project.getId()).get().getName()).isEqualTo("UpdatedProjectName");
    }

    @Test
    @DisplayName("[Project API] Usuwanie projektu - sukces")
    void shouldDeleteProject_Success() throws Exception {
        Project project = createProjectViaApi("ProjectToDelete");
        // Dodaj użytkownika do projektu, aby przetestować usuwanie powiązań
        User user = createUserViaApi("memberOfDeletedProject");
        Map<String, Long> assignPayload = new HashMap<>();
        assignPayload.put("userId", user.getId());
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignPayload)))
                .andExpect(status().isOk());


        mockMvc.perform(delete("/api/projects/" + project.getId()))
                .andExpect(status().isNoContent());
        assertThat(projectRepository.findById(project.getId())).isEmpty();
        // Sprawdź, czy użytkownik nadal istnieje (nie powinien być usunięty)
        assertThat(userRepository.findById(user.getId())).isPresent();
    }


    // --- Testy relacji Użytkownik-Projekt ---

    @Test
    @DisplayName("[Relation API] Przypisanie użytkownika do projektu - sukces")
    void shouldAssignUserToProject_Success() throws Exception {
        User user = createUserViaApi("assignUser");
        Project project = createProjectViaApi("ProjectForAssignment");
        Map<String, Long> assignPayload = new HashMap<>();
        assignPayload.put("userId", user.getId());

        mockMvc.perform(post("/api/projects/" + project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].username", is("assignUser")));

        Project projectFromDb = projectRepository.findById(project.getId()).orElseThrow();
        assertThat(projectFromDb.getUsers()).extracting(User::getUsername).contains("assignUser");
    }

    @Test
    @DisplayName("[Relation API] Przypisanie użytkownika do projektu - użytkownik już przypisany")
    void shouldNotAssignUserToProject_AlreadyAssigned() throws Exception {
        User user = createUserViaApi("assignedUser");
        Project project = createProjectViaApi("ProjectWithAssignedUser");
        Map<String, Long> assignPayload = new HashMap<>();
        assignPayload.put("userId", user.getId());

        // Pierwsze przypisanie
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignPayload)))
                .andExpect(status().isOk());

        // Próba drugiego przypisania tego samego użytkownika
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignPayload)))
                .andExpect(status().isConflict()) // Lub inny odpowiedni status, zależnie od implementacji serwisu
                .andExpect(content().string(containsString("jest już przypisany do projektu")));
    }


    @Test
    @DisplayName("[Relation API] Pobieranie użytkowników projektu")
    void shouldGetUsersFromProject() throws Exception {
        User user1 = createUserViaApi("member1");
        User user2 = createUserViaApi("member2");
        Project project = createProjectViaApi("ProjectWithMembers");

        Map<String, Long> assignPayload1 = Map.of("userId", user1.getId());
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(assignPayload1)));
        Map<String, Long> assignPayload2 = Map.of("userId", user2.getId());
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(assignPayload2)));

        MvcResult result = mockMvc.perform(get("/api/projects/" + project.getId() + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        // Deserializacja do Set<User>
        Set<User> users = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Set<User>>() {});
        assertThat(users).extracting(User::getUsername).containsExactlyInAnyOrder("member1", "member2");
    }

    @Test
    @DisplayName("[Relation API] Usunięcie użytkownika z projektu - sukces")
    void shouldRemoveUserFromProject_Success() throws Exception {
        User user = createUserViaApi("userToRemove");
        Project project = createProjectViaApi("ProjectForRemoval");
        Map<String, Long> assignPayload = Map.of("userId", user.getId());
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(assignPayload)));

        mockMvc.perform(delete("/api/projects/" + project.getId() + "/users/" + user.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + project.getId() + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("[Relation API] Usunięcie użytkownika z projektu - użytkownik nieprzypisany")
    void shouldNotRemoveUserFromProject_NotAssigned() throws Exception {
        User user = createUserViaApi("notAssignedUser");
        Project project = createProjectViaApi("ProjectForNonRemoval");

        mockMvc.perform(delete("/api/projects/" + project.getId() + "/users/" + user.getId()))
                .andExpect(status().isNotFound()) // Zakładając, że serwis rzuca ResourceNotFoundException
                .andExpect(content().string(containsString("nie jest przypisany do projektu")));
    }


    // --- Testy dla TaskController ---
    @Test
    @DisplayName("[Task API] Ustawienie priorytetu zadania - sukces")
    void shouldSetTaskPriority_Success() throws Exception {
        Task task = createTaskDirectly("Opis zadania do priorytetyzacji");

        mockMvc.perform(post("/api/tasks/" + task.getId() + "/priority/HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(task.getId().intValue())))
                .andExpect(jsonPath("$.priorityString", is("HIGH"))); // Zakładając, że Task ma getPriorityString()

        // Weryfikacja w bazie jest trudna dla @Transient, ale można sprawdzić obiekt zwrócony przez serwis
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        // assertThat(updatedTask.getPriorityString()).isEqualTo("HIGH"); // To nie zadziała dla @Transient po ponownym odczycie
    }

    @Test
    @DisplayName("[Task API] Ustawienie priorytetu zadania - nieprawidłowy poziom")
    void shouldNotSetTaskPriority_InvalidLevel() throws Exception {
        Task task = createTaskDirectly("Zadanie dla nieprawidłowego priorytetu");
        mockMvc.perform(post("/api/tasks/" + task.getId() + "/priority/VERY_HIGH"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Nieprawidłowy poziom priorytetu: VERY_HIGH")));
    }

    @Test
    @DisplayName("[Task API] Ustawienie priorytetu zadania - zadanie nie istnieje")
    void shouldNotSetTaskPriority_TaskNotFound() throws Exception {
        mockMvc.perform(post("/api/tasks/7777/priority/MEDIUM"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Nie znaleziono zadania o ID: 7777")));
    }
}