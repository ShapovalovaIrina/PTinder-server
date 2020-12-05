package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.trkpo.ptinder.config.Constants.USERS_PATH;

@RestController
@RequestMapping(USERS_PATH)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userRepository) {
        this.userService = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{googleId}")
    public User getCurrentUserInfo(@PathVariable("googleId") User user) {
        return userService.findUser(user);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("{googleId}")
    public User updateUsr(@PathVariable String googleId, @RequestBody User user) {
        return userService.updateUser(user, googleId);
    }

    @DeleteMapping("{googleId}")
    public void deleteUser(@PathVariable("googleId") String googleId) {
        userService.deleteUser(googleId);
    }

    @GetMapping("info/{googleId}")
    public boolean getUserInfoStatus(@PathVariable("googleId") String googleId) {
        return userService.getUserInfoStatus(googleId);
    }

    @PostMapping("info/{googleId}")
    public User setUserInfoStatus(@PathVariable("googleId") String googleId) {
        return userService.setUserInfoStatus(googleId);
    }

}
