package org.example.projectmanager.dto.user;

import org.example.projectmanager.entity.user.User;

public class UserDto {
    public Long id;
    public String username;

    public static UserDto MapFromEntity(User user) {
        var self = new UserDto();
        self.id = user.getId();
        self.username = user.getUsername();
        return self;
    }
}
