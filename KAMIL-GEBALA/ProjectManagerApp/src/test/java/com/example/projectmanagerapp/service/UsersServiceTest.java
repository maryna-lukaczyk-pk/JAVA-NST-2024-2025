package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersService = new UsersService(usersRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void getAllUsers() {
        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("TestUser1");

        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("TestUser2");

        when(usersRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> result = usersService.getAllUsers();

        assertEquals(2, result.size());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new user")
    void createUser() {
        Users user = new Users();
        user.setUsername("NewUser");

        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users result = usersService.createUser(user);

        assertNotNull(result);
        assertEquals("NewUser", result.getUsername());
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update an existing user")
    void updateUser_whenUserExists() {
        long userId = 1L;
        Users user = new Users();
        user.setId(userId);
        user.setUsername("UpdatedUser");

        when(usersRepository.existsById(userId)).thenReturn(true);
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users result = usersService.updateUser(userId, user);

        assertNotNull(result);
        assertEquals("UpdatedUser", result.getUsername());
        verify(usersRepository, times(1)).existsById(userId);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should delete a user")
    void deleteUser() {
        long userId = 1L;

        usersService.deleteUser(userId);

        verify(usersRepository, times(1)).deleteById(userId);
    }
}
