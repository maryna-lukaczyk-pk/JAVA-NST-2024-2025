//package com.example.projectmanagerapp.controller;
//
//import com.example.projectmanagerapp.entity.Users;
//import com.example.projectmanagerapp.repository.UserRepository;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    private UserRepository userRepository;
//
//    @GetMapping
//    public List<Users> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @PostMapping
//    public Users createUser(@RequestBody Users user) {
//        return userRepository.save(user);
//    }
//
//    @GetMapping("/{id}")
//    public Users getUserById(@PathVariable Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//}