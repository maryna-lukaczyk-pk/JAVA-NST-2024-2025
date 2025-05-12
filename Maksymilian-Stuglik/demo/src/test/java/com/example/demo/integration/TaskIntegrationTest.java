package com.example.demo.integration;

import com.example.demo.entity.Project;
import com.example.demo.entity.Tasks;
import com.example.demo.entity.Users;
import com.example.demo.priority.HighPriority;
import com.example.demo.priority.LowPriority;
import com.example.demo.priority.MediumPriority;
import com.example.demo.priority.PriorityLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TaskIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testTaskCrudOperations() throws Exception {
        Project project = new Project();
        project.setName("Project for Tasks");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);
        assertNotNull(createdProject.getId());

        Tasks task = new Tasks();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority("HIGH");
        task.setProject(createdProject);

        String taskJson = objectMapper.writeValueAsString(task);
        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andReturn();

        Tasks createdTask = objectMapper.readValue(createResult.getResponse().getContentAsString(), Tasks.class);
        assertNotNull(createdTask.getId());
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertEquals("HIGH", createdTask.getPriority());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.id == " + createdTask.getId() + ")].title").value("Test Task"))
                .andExpect(jsonPath("$[?(@.id == " + createdTask.getId() + ")].description").value("Test Description"))
                .andExpect(jsonPath("$[?(@.id == " + createdTask.getId() + ")].priority").value("HIGH"));

        createdTask.setTitle("Updated Task");
        createdTask.setDescription("Updated Description");
        createdTask.setPriority("MEDIUM");

        String updatedTaskJson = objectMapper.writeValueAsString(createdTask);
        mockMvc.perform(put("/api/tasks/{id}", createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));

        mockMvc.perform(delete("/api/tasks/{id}", createdTask.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + createdTask.getId() + ")]").isEmpty());
    }

    @Test
    public void testTaskPriorityLevels() throws Exception {
        Project project = new Project();
        project.setName("Project for Priority Tests");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);

        Tasks highTask = new Tasks();
        highTask.setTitle("High Priority Task");
        highTask.setDescription("This is a high priority task");
        highTask.setPriority("HIGH");
        highTask.setProject(createdProject);

        String highTaskJson = objectMapper.writeValueAsString(highTask);
        MvcResult highResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(highTaskJson))
                .andExpect(status().isOk())
                .andReturn();

        Tasks createdHighTask = objectMapper.readValue(highResult.getResponse().getContentAsString(), Tasks.class);
        assertEquals("HIGH", createdHighTask.getPriority());

        Tasks mediumTask = new Tasks();
        mediumTask.setTitle("Medium Priority Task");
        mediumTask.setDescription("This is a medium priority task");
        mediumTask.setPriority("MEDIUM");
        mediumTask.setProject(createdProject);

        String mediumTaskJson = objectMapper.writeValueAsString(mediumTask);
        MvcResult mediumResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mediumTaskJson))
                .andExpect(status().isOk())
                .andReturn();

        Tasks createdMediumTask = objectMapper.readValue(mediumResult.getResponse().getContentAsString(), Tasks.class);
        assertEquals("MEDIUM", createdMediumTask.getPriority());

        Tasks lowTask = new Tasks();
        lowTask.setTitle("Low Priority Task");
        lowTask.setDescription("This is a low priority task");
        lowTask.setPriority("LOW");
        lowTask.setProject(createdProject);

        String lowTaskJson = objectMapper.writeValueAsString(lowTask);
        MvcResult lowResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lowTaskJson))
                .andExpect(status().isOk())
                .andReturn();

        Tasks createdLowTask = objectMapper.readValue(lowResult.getResponse().getContentAsString(), Tasks.class);
        assertEquals("LOW", createdLowTask.getPriority());
    }

    @Test
    public void testPriorityLevelImplementations() throws Exception {
        PriorityLevel highPriority = new HighPriority();
        assertEquals("HIGH", highPriority.getPriority());

        PriorityLevel mediumPriority = new MediumPriority();
        assertEquals("MEDIUM", mediumPriority.getPriority());

        PriorityLevel lowPriority = new LowPriority();
        assertEquals("LOW", lowPriority.getPriority());

        Tasks task = new Tasks();
        task.setPriorityLevel(highPriority);
        assertEquals("HIGH", task.getPriority());

        task.setPriorityLevel(mediumPriority);
        assertEquals("MEDIUM", task.getPriority());

        task.setPriorityLevel(lowPriority);
        assertEquals("LOW", task.getPriority());
    }
}
