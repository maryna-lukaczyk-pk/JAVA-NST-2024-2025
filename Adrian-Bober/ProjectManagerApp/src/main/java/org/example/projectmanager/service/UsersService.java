package org.example.projectmanager.service;

import org.example.projectmanager.entity.Users;
import org.example.projectmanager.repository.UsersRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users createUsers(Users users) {
        return usersRepository.save(users);
    }

    public Users updateUsers(Long id, Users userDetails) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        users.setUsername(userDetails.getUsername());
        return usersRepository.save(users);
    }

    public void deleteUsers(Long id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        usersRepository.delete(users);
    }

    public Optional<Users> getUsersById(Long id) {
        return usersRepository.findById(id);
    }
}