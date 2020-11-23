package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listAll() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return "There are no users";
        }
        return "Hello, " + allUsers.get(0);
    }

    @GetMapping("/{googleId}")
    public @ResponseBody User showUser(@PathVariable String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        if (user == null) {
            return null;
        }
        return user;
    }

    @PostMapping
    public User putOne(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("id")
    public void deleteUser(@PathVariable("id") User user) {
        userRepository.delete(user);
    }
}
