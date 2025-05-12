package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users from repository")
    void testGetAllUsers() {
        // given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("TestUser1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // when
        List<User> users = userService.getAllUsers();

        //then
        assertEquals(2, users.size());
        assertEquals("TestUser1", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }
}