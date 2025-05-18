package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserCrudIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateUser() throws Exception {
        // Given
        User user = new User();
        user.setUsername("newuser");

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        User createdUser = objectMapper.readValue(
                result.getResponse().getContentAsString(), User.class);

        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("newuser");

        // Verify in database
        assertThat(userRepository.findById(createdUser.getId())).isPresent();
    }

    @Test
    @Transactional
    void shouldRetrieveUserById() throws Exception {
        // Given
        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        // When
        MvcResult result = mockMvc.perform(get("/api/v1/user/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        User retrievedUser = objectMapper.readValue(
                result.getResponse().getContentAsString(), User.class);

        assertThat(retrievedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(retrievedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/v1/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldRetrieveAllUsers() throws Exception {
        // Given
        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);

        // When
        MvcResult result = mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        User[] users = objectMapper.readValue(
                result.getResponse().getContentAsString(), User[].class);

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    @Transactional
    void shouldUpdateUser() throws Exception {
        // Given
        User user = new User();
        user.setUsername("originalname");
        User savedUser = userRepository.save(user);

        User updateData = new User();
        updateData.setUsername("updatedname");

        // When
        MvcResult result = mockMvc.perform(put("/api/v1/user/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        User updatedUser = objectMapper.readValue(
                result.getResponse().getContentAsString(), User.class);

        assertThat(updatedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(updatedUser.getUsername()).isEqualTo("updatedname");

        // Verify in database
        User dbUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertThat(dbUser).isNotNull();
        assertThat(dbUser.getUsername()).isEqualTo("updatedname");
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
        // Given
        User updateData = new User();
        updateData.setUsername("updatedname");

        // When/Then
        mockMvc.perform(put("/api/v1/user/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldDeleteUser() throws Exception {
        // Given
        User user = new User();
        user.setUsername("userToDelete");
        User savedUser = userRepository.save(user);

        // Verify user exists
        assertThat(userRepository.findById(savedUser.getId())).isPresent();

        // When
        mockMvc.perform(delete("/api/v1/user/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        // Then
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentUser() throws Exception {
        // When/Then
        mockMvc.perform(delete("/api/v1/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldReturnEmptyListWhenNoUsers() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        User[] users = objectMapper.readValue(
                result.getResponse().getContentAsString(), User[].class);

        assertThat(users).isEmpty();
    }

    @Test
    @Transactional
    void shouldCreateMultipleUsersWithUniqueIds() throws Exception {
        // Given/When/Then - Create first user
        User user1 = new User();
        user1.setUsername("user1");

        MvcResult result1 = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser1 = objectMapper.readValue(
                result1.getResponse().getContentAsString(), User.class);

        // Given/When/Then - Create second user
        User user2 = new User();
        user2.setUsername("user2");

        MvcResult result2 = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser2 = objectMapper.readValue(
                result2.getResponse().getContentAsString(), User.class);

        // Verify both users have unique IDs
        assertThat(createdUser1.getId()).isNotEqualTo(createdUser2.getId());
        assertThat(createdUser1.getUsername()).isEqualTo("user1");
        assertThat(createdUser2.getUsername()).isEqualTo("user2");

        // Verify both users exist in database
        assertThat(userRepository.findById(createdUser1.getId())).isPresent();
        assertThat(userRepository.findById(createdUser2.getId())).isPresent();
        assertThat(userRepository.findAll()).hasSize(2);
    }
}