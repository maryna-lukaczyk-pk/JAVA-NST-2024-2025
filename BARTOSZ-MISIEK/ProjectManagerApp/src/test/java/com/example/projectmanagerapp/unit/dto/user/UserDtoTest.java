package com.example.projectmanagerapp.unit.dto.user;

import org.example.projectmanager.dto.user.UserDto;
import org.example.projectmanager.entity.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDtoTest {
    @Test
    void CorrectlyMapsEntity() {
        var user = new User();
        user.setId(1L);
        user.setUsername("John Doe");

        var userDto = UserDto.MapFromEntity(user);

        Assertions.assertEquals(user.getId(), userDto.id);
        Assertions.assertEquals(user.getUsername(), userDto.username);
    }
}
