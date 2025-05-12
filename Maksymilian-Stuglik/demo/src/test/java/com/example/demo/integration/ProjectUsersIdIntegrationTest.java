package com.example.demo.integration;

import com.example.demo.entity.Project;
import com.example.demo.entity.ProjectUsers;
import com.example.demo.entity.ProjectUsersId;
import com.example.demo.entity.Users;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProjectUsersIdIntegrationTest {

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

    @Autowired
    private DataSource dataSource;

    @Test
    public void testProjectUsersIdEquality() throws Exception {
        Project project1 = new Project();
        project1.setName("Project 1");

        String project1Json = objectMapper.writeValueAsString(project1);
        MvcResult project1Result = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(project1Json))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject1 = objectMapper.readValue(project1Result.getResponse().getContentAsString(), Project.class);

        Project project2 = new Project();
        project2.setName("Project 2");

        String project2Json = objectMapper.writeValueAsString(project2);
        MvcResult project2Result = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(project2Json))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject2 = objectMapper.readValue(project2Result.getResponse().getContentAsString(), Project.class);

        Users user1 = new Users();
        user1.setUsername("User 1");

        String user1Json = objectMapper.writeValueAsString(user1);
        MvcResult user1Result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user1Json))
                .andExpect(status().isOk())
                .andReturn();

        Users createdUser1 = objectMapper.readValue(user1Result.getResponse().getContentAsString(), Users.class);

        Users user2 = new Users();
        user2.setUsername("User 2");

        String user2Json = objectMapper.writeValueAsString(user2);
        MvcResult user2Result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user2Json))
                .andExpect(status().isOk())
                .andReturn();

        Users createdUser2 = objectMapper.readValue(user2Result.getResponse().getContentAsString(), Users.class);

        ProjectUsersId id1 = new ProjectUsersId(createdProject1.getId(), createdUser1.getId());
        ProjectUsersId id2 = new ProjectUsersId(createdProject1.getId(), createdUser1.getId());
        ProjectUsersId id3 = new ProjectUsersId(createdProject2.getId(), createdUser1.getId());
        ProjectUsersId id4 = new ProjectUsersId(createdProject1.getId(), createdUser2.getId());

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertNotEquals(id1, id4);

        assertEquals(id1.hashCode(), id2.hashCode());

        Set<ProjectUsersId> idSet = new HashSet<>();
        idSet.add(id1);
        assertTrue(idSet.contains(id2));
        assertFalse(idSet.contains(id3));
        assertFalse(idSet.contains(id4));

        ProjectUsersId idWithNullProject = new ProjectUsersId(null, createdUser1.getId());
        ProjectUsersId idWithNullUser = new ProjectUsersId(createdProject1.getId(), null);

        assertNotEquals(id1, idWithNullProject);
        assertNotEquals(id1, idWithNullUser);
        assertNotEquals(idWithNullProject, idWithNullUser);

        assertNotEquals(id1, "Not a ProjectUsersId");
    }

    @Test
    public void testTaskPriorityLevels() throws Exception {
        Project project = new Project();
        project.setName("Priority Test Project");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn();

        Project createdProject = objectMapper.readValue(projectResult.getResponse().getContentAsString(), Project.class);

        String[] priorities = {"HIGH", "MEDIUM", "LOW"};

        for (String priority : priorities) {
            String taskJson = String.format(
                    "{\"title\":\"Task with %s priority\",\"description\":\"Testing %s priority\",\"priority\":\"%s\",\"project\":{\"id\":%d}}",
                    priority, priority, priority, createdProject.getId());

            mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(taskJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.priority").value(priority));
        }

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT priority FROM tasks");
            ResultSet rs = stmt.executeQuery();

            Set<String> foundPriorities = new HashSet<>();
            while (rs.next()) {
                foundPriorities.add(rs.getString("priority"));
            }

            assertEquals(3, foundPriorities.size());
            assertTrue(foundPriorities.contains("HIGH"));
            assertTrue(foundPriorities.contains("MEDIUM"));
            assertTrue(foundPriorities.contains("LOW"));
        }
    }
}
