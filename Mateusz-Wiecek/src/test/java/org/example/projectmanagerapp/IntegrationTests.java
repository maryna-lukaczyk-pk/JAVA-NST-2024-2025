package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationTests {

    // 1) Definicja kontenera PostgreSQL
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // 2) Nadpisujemy wszystkie właściwości datasource/JPA, żeby korzystać z Testcontainers
    @DynamicPropertySource
    static void overrideDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",               postgres::getJdbcUrl);
        registry.add("spring.datasource.username",          postgres::getUsername);
        registry.add("spring.datasource.password",          postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("jakarta.persistence.jdbc.url",        postgres::getJdbcUrl);
        registry.add("spring.jpa.properties.hibernate.dialect",
                () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired private MockMvc mvc;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserRepository    userRepository;
    @Autowired private TaskRepository    taskRepository;

    @BeforeEach
    void cleanDatabase() {
        // Czyścimy kolejność: Task → Project → User
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void projectCrudFlow() throws Exception {
        // CREATE
        mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestProject\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/projects/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("TestProject")));

        // READ ALL
        mvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("TestProject")));

        // READ ONE
        Long projectId = projectRepository.findAll().get(0).getId();
        mvc.perform(get("/api/projects/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",   is(projectId.intValue())))
                .andExpect(jsonPath("$.name", is("TestProject")));

        // UPDATE
        mvc.perform(put("/api/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestProjectUpdated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestProjectUpdated")));

        // DELETE
        mvc.perform(delete("/api/projects/{id}", projectId))
                .andExpect(status().isNoContent());

        // VERIFY DELETION
        mvc.perform(get("/api/projects/{id}", projectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void userCrudFlow() throws Exception {
        // CREATE
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"u1\",\"password\":\"p1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is("u1")));

        // READ ALL
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        Long userId = userRepository.findAll().get(0).getId();

        // READ ONE
        mvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("u1")));

        // UPDATE
        mvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"u2\",\"password\":\"p2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("u2")));

        // DELETE
        mvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());

        // VERIFY DELETION
        mvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void taskCrudFlow() throws Exception {
        // Przygotuj najpierw zależności: projekt i użytkownika
        Project p = projectRepository.save(new Project(null, "Proj1"));
        User    u = userRepository.save(new User(null, "user1"));

        // CREATE (zagnieżdżone JSON: title, description, taskType, project)
        String jsonCreate = String.format(
                "{\"title\":\"Task1\",\"description\":\"desc\",\"taskType\":\"BUG\",\"project\":{\"id\":%d}}",
                p.getId()
        );
        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",       notNullValue()))
                .andExpect(jsonPath("$.title",    is("Task1")))
                .andExpect(jsonPath("$.description", is("desc")))
                .andExpect(jsonPath("$.taskType", is("BUG")))
                .andExpect(jsonPath("$.project.id", is(p.getId().intValue())));

        // READ ALL
        mvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        Long taskId = taskRepository.findAll().get(0).getId();

        // READ ONE
        mvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",       is("Task1")))
                .andExpect(jsonPath("$.project.id",   is(p.getId().intValue())));

        // UPDATE (zmieniamy title, description i taskType)
        String jsonUpdate = String.format(
                "{\"title\":\"Task1-upd\",\"description\":\"newdesc\",\"taskType\":\"FEATURE\",\"project\":{\"id\":%d}}",
                p.getId()
        );
        mvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title",      is("Task1-upd")))
                .andExpect(jsonPath("$.description",is("newdesc")))
                .andExpect(jsonPath("$.taskType",   is("FEATURE")))
                .andExpect(jsonPath("$.project.id", is(p.getId().intValue())));

        // DELETE
        mvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        // VERIFY DELETION
        mvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }
}
