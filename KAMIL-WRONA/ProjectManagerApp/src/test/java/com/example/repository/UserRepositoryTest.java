package com.example.repository;

import com.example.ProjectManagerAppApplication;
import com.example.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveAndFindUser() {
        User existing = userRepository.findByUsername("TestUserRepo");
        if (existing != null) {
            userRepository.delete(existing);
        }

        User user = new User();
        user.setUsername("TestUserRepo");

        User saved = userRepository.save(user);
        userRepository.flush();

        assertNotNull(saved.getId());
        assertEquals("TestUserRepo", saved.getUsername());
    }
}
