package org.example.projectmanager.dto.user;

import org.example.projectmanager.entity.user.User;

import java.util.Objects;

public class UserDto {
    public Long id;
    public String username;

    public static UserDto MapFromEntity(User user) {
        var self = new UserDto();
        self.id = user.getId();
        self.username = user.getUsername();
        return self;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserDto userDto)) return false;
        return Objects.equals(id, userDto.id) && Objects.equals(username, userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
