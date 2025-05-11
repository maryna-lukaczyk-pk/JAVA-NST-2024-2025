package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;
    @Captor private ArgumentCaptor<User> captor;

    private User u1, u2;

    @BeforeEach
    void setUp() {
        u1 = new User(); u1.setId(1L); u1.setUsername("alice");
        u2 = new User(); u2.setId(2L); u2.setUsername("bob");
    }

    @Test
    void findAll_returnsAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> result = userService.findAll();
        assertThat(result).containsExactly(u1, u2);
        verify(userRepository).findAll();
    }

    @Test
    void findById_existing_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(u1));

        Optional<User> result = userService.findById(1L);
        assertThat(result).contains(u1);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(99L);
        assertThat(result).isEmpty();
        verify(userRepository).findById(99L);
    }

    @Test
    void create_savesAndReturns() {
        User newUser = new User(); newUser.setUsername("charlie");
        when(userRepository.save(any(User.class))).thenReturn(u1);

        User result = userService.create(newUser);
        assertThat(result).isEqualTo(u1);
        verify(userRepository).save(newUser);
    }

    @Test
    void update_setsIdAndSaves() {
        User upd = new User(); upd.setUsername("updated");
        when(userRepository.save(any(User.class))).thenReturn(upd);

        User result = userService.update(5L, upd);
        assertThat(result).isEqualTo(upd);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(5L);
    }

    @Test
    void delete_invokesRepository() {
        doNothing().when(userRepository).deleteById(1L);
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }
}
