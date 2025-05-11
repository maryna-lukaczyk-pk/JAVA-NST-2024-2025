package com.example.projectmanagerapp.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAll_ReturnsAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        // Wywołanie metody serwisu
        List<User> result = userService.getAll();

        assertEquals(mockUsers, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingUser_ReturnsUser() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getById(1);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getById_NonExistingUser_ThrowsException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getById(1));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void create_User_SavesAndReturnsUser() {
        User newUser = new User();
        User savedUser = new User();
        when(userRepository.save(newUser)).thenReturn(savedUser);

        User result = userService.create(newUser);

        assertEquals(savedUser, result);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void update_ExistingUser_UpdatesAndReturnsUser() {
        User updates = new User();
        User updatedUser = new User();
        updatedUser.setId(1);
        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(1, updates);

        assertEquals(updatedUser, result);
        assertEquals(1, result.getId());
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void update_NonExistingUser_ThrowsException() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> userService.update(1, new User()));
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void delete_ExistingUser_DeletesUser() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.delete(1);

        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_NonExistingUser_ThrowsException() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> userService.delete(1));
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, never()).deleteById(anyInt());
    }
}
