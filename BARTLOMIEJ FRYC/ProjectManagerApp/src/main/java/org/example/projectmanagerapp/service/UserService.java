package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users updateUser(Long id, Users userDetails) {
        Optional<Users> user = userRepository.findById(id);
        if (user.isPresent()) {
            Users existingUser = user.get();
            // Update the user fields with the new details
            if (userDetails.getUser_name() != null) {
                existingUser.setUser_name(userDetails.getUser_name());
            }
            if (userDetails.getProjects() != null) {
                existingUser.setProjects(userDetails.getProjects());
            }

            return userRepository.save(existingUser);
        }
        return null; // Or throw an exception
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
