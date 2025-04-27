package org.example.projectmanager.dto.user;

import org.example.projectmanager.entity.user.User;

public class UserEditDto {
    public Long id;
    public String username;

    public void UpdateEntity(User user) {
        user.setId(id);
        user.setUsername(username);
    }
}
