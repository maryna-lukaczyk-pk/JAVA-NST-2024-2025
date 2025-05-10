package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repozytorium.UsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public Users getUserById(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        return user.orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }
    public Users updateUser(Long id, Users usersDetails) {
        return usersRepository.findById(id)
                .map(users -> {
                    users.setUsername(usersDetails.getUsername());
                    return usersRepository.save(users);
                })
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("User not found with id: " + id);
        }
    }

}
