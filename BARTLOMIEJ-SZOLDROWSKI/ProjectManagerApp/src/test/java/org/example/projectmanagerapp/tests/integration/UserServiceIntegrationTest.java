package org.example.projectmanagerapp.tests.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userShouldPersistInRealDatabase() {
        User user = new User();
        user.setUsername("container_user");
        user = userRepository.save(user);

        User savedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("container_user", savedUser.getUsername());
    }
}
