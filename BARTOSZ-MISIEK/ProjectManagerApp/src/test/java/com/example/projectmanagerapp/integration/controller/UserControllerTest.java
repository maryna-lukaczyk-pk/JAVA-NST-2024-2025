package com.example.projectmanagerapp.integration.controller;

import com.example.projectmanagerapp.integration.config.TestDatabaseConfig;
import com.example.projectmanagerapp.integration.utilities.AssertionHelper;
import org.example.projectmanager.dto.user.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanager.dto.user.UserCreateDto;
import org.example.projectmanager.dto.user.UserEditDto;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String BASE_URL = "/api/users";

    private String GetIdUrl(Long id) {
        return BASE_URL + "/" + id;
    }

    private Long CreateUser(UserCreateDto userCreateDto) throws Exception {
        String json = objectMapper.writeValueAsString(userCreateDto);
        String response2 = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return Long.valueOf(response2);
    }

    @Test
    public void testUserGetAll() throws Exception {
        UserCreateDto dto1 = new UserCreateDto();
        dto1.username = "user1";
        Long id1 = CreateUser(dto1);

        UserCreateDto dto2 = new UserCreateDto();
        dto2.username = "user2";
        Long id2 = CreateUser(dto2);

        var createdUser1 = getUserDto(id1);
        var createdUser2 = getUserDto(id2);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    UserDto[] users = objectMapper.readValue(content, UserDto[].class);
                    boolean found1 = Arrays.asList(users).contains(createdUser1);
                    boolean found2 = Arrays.asList(users).contains(createdUser2);

                    Assertions.assertTrue(found1);
                    Assertions.assertTrue(found2);
                });
    }

    @Test
    public void testUserCreation() throws Exception {
        UserCreateDto dto = new UserCreateDto();
        dto.username = "testuser";
        Long id = CreateUser(dto);

        Assertions.assertTrue(id > 0);
    }

    @Test
    public void testUserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(99999L)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUserGetAndDelete() throws Exception {
        var username = "integrationuser";

        UserCreateDto dto = new UserCreateDto();
        dto.username = username;

        Long id = CreateUser(dto);
        UserDto user = getUserDto(id);
        Assertions.assertEquals(id, user.id);
        Assertions.assertEquals(username, user.username);

        mockMvc.perform(MockMvcRequestBuilders.delete(GetIdUrl(id)))
                .andExpect(status().isOk())
                .andExpect(AssertionHelper::AssertIsEmpty);

        mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(id)))
                .andExpect(status().isBadRequest());
    }

    private UserDto getUserDto(Long id) throws Exception {
        String content = mockMvc.perform(MockMvcRequestBuilders.get(GetIdUrl(id)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, UserDto.class);
    }

    @Test
    public void testUserUpdate() throws Exception {
        UserCreateDto createDto = new UserCreateDto();
        createDto.username = "toupdate";
        Long id = CreateUser(createDto);

        UserEditDto editDto = new UserEditDto();
        editDto.id = id;
        editDto.username = "updateduser";
        String updateJson = objectMapper.writeValueAsString(editDto);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(AssertionHelper::AssertIsEmpty);

        UserDto user = getUserDto(id);
        Assertions.assertEquals(id, user.id);
        Assertions.assertEquals("updateduser", user.username);
    }
}
