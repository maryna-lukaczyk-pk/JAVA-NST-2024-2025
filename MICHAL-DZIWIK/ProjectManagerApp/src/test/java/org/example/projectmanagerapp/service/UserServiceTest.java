package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("getAllUsers should return list of all users")
    void getAllUsers_returnsAll() {
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("TestUser1");
        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("TestUser2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getUserById when found returns user")
    void getUserById_found_returnsUser() {
        User u = new User();
        u.setId(1L);
        u.setUsername("TestUser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        User result = userService.getUserById(1L);

        assertThat(result).isEqualTo(u);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("getUserById when not found returns null")
    void getUserById_notFound_returnsNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUserById(1L);

        assertThat(result).isNull();
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("createUser should save and return user")
    void createUser_savesAndReturns() {
        User u = new User();
        u.setUsername("NewUser");
        User saved = new User();
        saved.setId(1L);
        saved.setUsername("NewUser");
        when(userRepository.save(u)).thenReturn(saved);

        User result = userService.createUser(u);

        assertThat(result).isEqualTo(saved);
        verify(userRepository).save(u);
    }

    @Test
    @DisplayName("updateUser when exists updates and returns")
    void updateUser_exists_updatesAndReturns() {
        User existing = new User();
        existing.setId(1L);
        existing.setUsername("Old");
        User details = new User();
        details.setUsername("Updated");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.updateUser(1L, details);

        assertThat(result.getUsername()).isEqualTo("Updated");
        verify(userRepository).findById(1L);
        verify(userRepository).save(existing);
    }

    @Test
    @DisplayName("updateUser when not exists returns null")
    void updateUser_notExists_returnsNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.updateUser(1L, new User());

        assertThat(result).isNull();
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteUser when exists deletes")
    void deleteUser_exists_deletes() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUser when not exists does nothing")
    void deleteUser_notExists_noDelete() {
        when(userRepository.existsById(1L)).thenReturn(false);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
