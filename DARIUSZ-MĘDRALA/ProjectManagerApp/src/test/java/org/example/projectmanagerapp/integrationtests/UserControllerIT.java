package org.example.projectmanagerapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testGetUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("Dariusz");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("Darek");
        userRepository.save(user2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("Dariusz"))
                .andExpect(jsonPath("$[1].username").value("Darek"));
    }

    @Test
    void testAddUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("NewUser");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("NewUser"));
    }

    @Test
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("ToDelete");
        user = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete/" + user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("OldName");
        user = userRepository.save(user);

        User updatedUser = new User();
        updatedUser.setUsername("NewName");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("NewName"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setUsername("FindMe");
        user = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("FindMe"));
    }
}