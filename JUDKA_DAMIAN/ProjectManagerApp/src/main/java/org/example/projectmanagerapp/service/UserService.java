package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.schemas.UserDTO;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Integer id) throws NotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public void addUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());

        userRepository.save(user);
    }

    public void deleteUser(int id) throws NotFoundException {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException();
        }
    }

    public void updateUser(UserDTO dto, int id) throws NotFoundException {
        if(userRepository.existsById(id)) {
            User user = new User();
            user.setId(id);
            user.setUsername(dto.getUsername());
            userRepository.save(user);
        } else {
            throw new NotFoundException();
        }
    }
}
