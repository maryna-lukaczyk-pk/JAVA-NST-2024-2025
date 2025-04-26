package s.kopec.ProjectManagerApp.controller;

import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.entity.User;
import s.kopec.ProjectManagerApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @PostMapping("/create")
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

}
