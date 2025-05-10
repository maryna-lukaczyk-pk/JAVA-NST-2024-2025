package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        Users user1 = new Users();
        user1.setUsername("TestUser1");

        Users user2 = new Users();
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }
}
