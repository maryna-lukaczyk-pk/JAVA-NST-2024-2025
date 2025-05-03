package org.example.projectmanager.service;

import org.example.projectmanager.entity.Users;
import org.example.projectmanager.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private UsersService usersService;

    private Users user1;
    private Users user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new Users();
        user1.setId(1L);
        user1.setUsername("john_doe");

        user2 = new Users();
        user2.setId(2L);
        user2.setUsername("jane_doe");
    }

    @Test
    void getAllUsers() {
        when(usersRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = usersService.getAllUsers();

        assertEquals(2, users.size());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void createUser() {
        when(usersRepository.save(any(Users.class))).thenReturn(user1);

        Users createdUser = usersService.createUsers(user1);

        assertEquals("john_doe", createdUser.getUsername());
        verify(usersRepository, times(1)).save(user1);
    }

    @Test
    void getUserById() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<Users> foundUser = usersService.getUsersById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("john_doe", foundUser.get().getUsername());
    }
}