package com.example.projectmanagerapp.service;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UsersRepository;
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

    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    public Users updateUser(long id, Users user) {
        if(usersRepository.existsById(id)){
            return usersRepository.save(user);
        }
        return null;
    }

    public void deleteUser(long id) {usersRepository.deleteById(id); }

}
