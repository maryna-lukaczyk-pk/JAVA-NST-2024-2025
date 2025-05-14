package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Tag("integration")
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanup() {

        jdbcTemplate.update("DELETE FROM project_users");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM project");
        jdbcTemplate.update("DELETE FROM tasks");
    }

    @Test
    void userCrudFlow() throws Exception {

        Users user = new Users();
        user.setUsername("testuser_" + java.util.UUID.randomUUID());
        String userJson = objectMapper.writeValueAsString(user);

        String resp = mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(resp, Users.class).getId();

        // READ
        mvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())));

        // UPDATE
        Users upd = new Users();
        upd.setUsername("updUser_" + java.util.UUID.randomUUID());
        String updJson = objectMapper.writeValueAsString(upd);
        mvc.perform(put("/api/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(upd.getUsername())));

        // DELETE
        mvc.perform(delete("/api/users/" + id))
                .andExpect(status().isNoContent());

        // NOT FOUND
        mvc.perform(get("/api/users/" + id))
                .andExpect(status().isNotFound());
    }
}