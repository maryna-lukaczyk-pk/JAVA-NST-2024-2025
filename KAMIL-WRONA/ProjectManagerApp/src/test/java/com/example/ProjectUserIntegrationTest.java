package com.example.integration;

import com.example.entity.Project;
import com.example.entity.User;
import com.example.repository.ProjectRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProjectUserIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void shouldAssignUserToProject() throws Exception {

        User user = new User();
        user.setUsername("TestUser");
        user = userRepository.save(user);

        Project project = new Project();
        project.setName("TestProject");
        project.setDescription("Integration test");
        project = projectRepository.save(project);


        mockMvc.perform(put("/projects/" + project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(user.getId()))))
                .andExpect(status().isOk());


        Project updated = projectRepository.findById(project.getId()).orElseThrow();
        assertThat(updated.getUsers()).extracting(User::getUsername).contains("TestUser");
    }
}
