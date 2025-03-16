package org.example.projectmanagerapp.controllers;

import org.example.projectmanagerapp.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @GetMapping()
    public User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        return user;
    }
}
