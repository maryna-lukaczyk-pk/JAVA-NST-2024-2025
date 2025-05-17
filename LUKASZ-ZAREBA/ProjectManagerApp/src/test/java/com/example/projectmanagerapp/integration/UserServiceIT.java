package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIT extends BaseIT {

    @Autowired
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("User1");
        userService.create(user1);

        User user2 = new User();
        user2.setUsername("User2");
        userService.create(user2);

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> "User1".equals(u.getUsername())));
        assertTrue(users.stream().anyMatch(u -> "User2".equals(u.getUsername())));
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setUsername("TestUser");
        User savedUser = userService.create(user);

        User retrievedUser = userService.getById(savedUser.getId());

        assertNotNull(retrievedUser);
        assertEquals(savedUser.getId(), retrievedUser.getId());
        assertEquals("TestUser", retrievedUser.getUsername());
    }

    @Test
    public void testGetUserByIdNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.getById(9999);
        });
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("UserToDelete");
        User savedUser = userService.create(user);

        assertNotNull(userService.getById(savedUser.getId()));
        userService.delete(savedUser.getId());

        assertThrows(NoSuchElementException.class, () -> {
            userService.getById(savedUser.getId());
        });
    }

    @Test
    public void testDeleteUserNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.delete(9999);
        });
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("OriginalUsername");
        User savedUser = userService.create(user);

        User updates = new User();
        updates.setUsername("UpdatedUsername");

        User updatedUser = userService.update(savedUser.getId(), updates);

        assertNotNull(updatedUser);
        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals("UpdatedUsername", updatedUser.getUsername());
    }

    @Test
    public void testUpdateUserNotFound() {
        User updates = new User();
        updates.setUsername("UpdatedUsername");

        assertThrows(NoSuchElementException.class, () -> {
            userService.update(9999, updates);
        });
    }
}