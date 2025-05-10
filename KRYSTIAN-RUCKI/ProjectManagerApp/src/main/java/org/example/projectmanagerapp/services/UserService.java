package org.example.projectmanagerapp.services;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRep) {
        this.userRepository = userRep;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public Users updateUser(long userId, Users updatedUser) {
        Users existingUser = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        existingUser.setUsername(updatedUser.getUsername());
        return userRepository.save(existingUser);
    }
}
