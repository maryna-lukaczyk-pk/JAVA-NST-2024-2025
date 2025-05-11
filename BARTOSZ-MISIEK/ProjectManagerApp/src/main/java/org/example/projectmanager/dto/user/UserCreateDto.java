package org.example.projectmanager.dto.user;

import org.example.projectmanager.entity.user.User;

public class UserCreateDto {
    public String username;

    public User MapToEntity() {
        User user = new User();
        user.setUsername(username);
        return user;
    }
}
