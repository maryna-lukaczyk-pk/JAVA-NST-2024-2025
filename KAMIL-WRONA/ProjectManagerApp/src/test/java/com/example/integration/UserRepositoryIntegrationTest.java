package com.example.integration;

import com.example.ProjectManagerAppApplication;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        User existingUser = userRepository.findByUsername("integration_test_user");
        if (existingUser != null) {
            userRepository.delete(existingUser);
            userRepository.flush();
        }
    }

    @Test
    void shouldSaveAndFetchUser() {
        User user = new User();
        user.setUsername("integration_test_user");
        User saved = userRepository.save(user);

        Optional<User> result = userRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("integration_test_user", result.get().getUsername());
    }
}
