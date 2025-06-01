package org.example.projectmanagerapp.tests.security;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

    @Test
    @DisplayName("Gdy użytkownik istnieje, powinien zostać poprawnie załadowany")
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        User user = new User("jan", "encodedPass", "jan@example.com", User.UserRole.USER);
        when(userRepository.findByUsername("jan")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("jan");

        assertEquals("jan", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(
                authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Gdy użytkownik nie istnieje, powinien zostać rzucony wyjątek")
    void loadUserByUsername_UserDoesNotExist_ThrowsException() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("ghost");
        });
    }
}

