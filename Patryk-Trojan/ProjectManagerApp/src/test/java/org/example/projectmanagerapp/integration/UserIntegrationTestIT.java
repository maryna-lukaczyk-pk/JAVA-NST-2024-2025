package org.example.projectmanagerapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ITTestConfiguration.class})
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldCDeleteUserByID() throws Exception {
        // given
        User user = new User();
        user.setUsername("username");
        userRepository.save(user);

        // when
        MvcResult mvcResult = mockMvc.perform(delete("/api/user/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then


        List<User> all = userRepository.findAll();
        assertThat(all.isEmpty()).isTrue();


    }
    @Test
    void shouldCDeleteAll() throws Exception {
        // given
        User user = new User();
        User user1 = new User();
        User user2= new User();
        User user3 = new User();

        user.setUsername("username");
        user1.setUsername("username");
        user2.setUsername("username");
        user3.setUsername("username");

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // when
        MvcResult mvcResult = mockMvc.perform(delete("/api/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then


        List<User> all = userRepository.findAll();
        assertThat(all.isEmpty()).isTrue();


    }

    @Test
    void shouldCFindUserByID() throws Exception {
        // given
        User user = new User();
        user.setUsername("username");
        userRepository.save(user);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/user/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then

        User testUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertThat(testUser).isNotNull();
        assertThat(testUser.getUsername()).isEqualTo(user.getUsername());
        User userFromDB = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(user.getId()).isEqualTo(userFromDB.getId());


    }

    @Test
    void shouldCreateUser() throws Exception {
        // given
        User user = new User();
        user.setUsername("username");

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/user").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then

        User testUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertThat(testUser).isNotNull();
        assertThat(testUser.getUsername()).isEqualTo(user.getUsername());
        User userFromDB = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(user.getUsername()).isEqualTo(userFromDB.getUsername());


    }

    @Test
    void shouldCFindAllUser() throws Exception {
        // given
        User user = new User();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();


        user.setUsername("username");
        user1.setUsername("username1");
        user2.setUsername("username2");
        user3.setUsername("username3");

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        // then


        String response = mvcResult.getResponse().getContentAsString();
        List<User> returnedUsers = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));

        List<User> all = userRepository.findAll();
        for (User u : all) {
            System.out.printf(u.getUsername() + " all" + "\n");
        }
        for (User u : returnedUsers) {
            System.out.printf(u.getUsername() + " returned" + "\n");
        }
        assertThat(returnedUsers.get(0).getUsername()).isEqualTo("username");
        assertThat(returnedUsers.get(1).getUsername()).isEqualTo("username1");
        assertThat(returnedUsers.get(2).getUsername()).isEqualTo("username2");
        assertThat(returnedUsers.get(3).getUsername()).isEqualTo("username3");
        assertThat(returnedUsers.size()).isEqualTo(4);

    }

    @Test
    void shouldUpdateUser() throws Exception {
        // given
        User existingUser = new User();
        existingUser.setUsername("oldUsername");
        User savedUser = userRepository.save(existingUser); // zapisujemy do bazy

        User updatedUser = new User();
        updatedUser.setUsername("newUsername");

        // when
        mockMvc.perform(put("/api/user/" + savedUser.getId())
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Optional<User> result = userRepository.findById(savedUser.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("newUsername");
    }

}
