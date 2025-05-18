package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
public class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateAndGetUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        String userJson = objectMapper.writeValueAsString(user);

        String responseContent = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User createdUser = objectMapper.readValue(responseContent, User.class);

        mockMvc.perform(get("/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("originalname");
        User savedUser = userRepository.save(user);

        User update = new User();
        update.setUsername("updatedname");

        mockMvc.perform(put("/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("updatedname"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("tobedeleted");
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/users/" + savedUser.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/" + savedUser.getId()))
                .andExpect(status().isNotFound());

        assertFalse(userRepository.findById(savedUser.getId()).isPresent());
    }

    @Test
    public void testGetNonExistentUser() throws Exception {
        mockMvc.perform(get("/users/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNonExistentUser() throws Exception {
        User update = new User();
        update.setUsername("Updated Username");

        mockMvc.perform(put("/users/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNonExistentUser() throws Exception {
        mockMvc.perform(delete("/users/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllUsersEmpty() throws Exception {
        userRepository.deleteAll();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}