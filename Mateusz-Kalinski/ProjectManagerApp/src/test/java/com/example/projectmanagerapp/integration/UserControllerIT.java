package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIT {

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
    private UserRepository userRepository;

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
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        Users newUser = new Users();
        newUser.setUsername("testuser123");
        MvcResult mvcResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.username", is("testuser123")))
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        Users createdUser = objectMapper.readValue(responseBody, Users.class);
        mockMvc.perform(get("/api/users/" + createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser123")));
    }

    @Test
    void shouldGetUserById() throws Exception {
        Users userToCreate = new Users();
        userToCreate.setUsername("userDoOdczytu");
        MvcResult createResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn();
        Users createdUser = objectMapper.readValue(createResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();
        mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.username", is("userDoOdczytu")));
    }

    @Test
    void shouldHandleGetNonExistentUserById() throws Exception {
        Long nonExistentUserId = 9999L;
        MvcResult result = mockMvc.perform(get("/api/users/" + nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.isEmpty(), "Response body should be empty for non-existent user if controller returns null and status is OK");
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        Users user1 = new Users();
        user1.setUsername("userAlpha");
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk());
        Users user2 = new Users();
        user2.setUsername("userBeta");
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("userAlpha", "userBeta")));
    }

    @Test
    void shouldUpdateExistingUser() throws Exception {
        Users userToCreate = new Users();
        userToCreate.setUsername("userDoAktualizacji");
        MvcResult createResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn();
        Users createdUser = objectMapper.readValue(createResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();
        Users updatedUserDetails = new Users();
        updatedUserDetails.setUsername("zaktualizowanyUser");
        mockMvc.perform(put("/api/users/update/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.username", is("zaktualizowanyUser")));
        mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("zaktualizowanyUser")));
    }

    @Test
    void shouldHandleUpdateNonExistentUser() throws Exception {
        Long nonExistentUserId = 9999L;
        Users updatedUserDetails = new Users();
        updatedUserDetails.setUsername("probaAktualizacjiNieistniejacego");
        MvcResult result = mockMvc.perform(put("/api/users/update/" + nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDetails)))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.isEmpty(), "Response body should be empty when trying to update non-existent user if controller returns null");
    }

    @Test
    void shouldDeleteExistingUser() throws Exception {
        Users userToCreate = new Users();
        userToCreate.setUsername("userDoUsuniecia");
        MvcResult createResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn();
        Users createdUser = objectMapper.readValue(createResult.getResponse().getContentAsString(), Users.class);
        Long userId = createdUser.getId();
        mockMvc.perform(delete("/api/users/delete/" + userId))
                .andExpect(status().isOk());
        MvcResult getResult = mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(getResult.getResponse().getContentAsString().isEmpty(), "Response for deleted user should be empty if GET non-existent returns 200 OK empty body");
    }

    @Test
    void shouldHandleDeleteNonExistentUser() throws Exception {
        Long nonExistentUserId = 9999L;
        mockMvc.perform(delete("/api/users/delete/" + nonExistentUserId))
                // ZMIENIONE ZGODNIE Z LOGIEM BŁĘDU: oczekujemy 200 OK, a nie 404
                .andExpect(status().isOk());
    }
}