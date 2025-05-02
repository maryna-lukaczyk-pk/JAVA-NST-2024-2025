package org.example.projectmanager.service;

import org.example.projectmanager.entity.Users;
import org.example.projectmanager.repository.UsersRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users createUser(Users users) {
        return usersRepository.save(users);
    }
}
