package s.kopec.ProjectManagerApp.service;

import org.springframework.stereotype.Service;
import s.kopec.ProjectManagerApp.entity.User;
import s.kopec.ProjectManagerApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUserName(Long id, String newUserName) {
        if (userRepository.existsById(id)){
            User myUser = userRepository.getReferenceById(id);
            myUser.setUsername(newUserName);
            userRepository.save(myUser);
        }
    }
}
