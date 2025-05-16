package com.example.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserSettersAndGetters() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
    }
}
