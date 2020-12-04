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

    @GetMapping("{id}")
    public User getCurrentUserInfo(@PathVariable("id") User user) {
        Optional<User> currentUser = userService.findUser(user);
        return currentUser.orElseGet(User::new);
    }

    @GetMapping("google/{googleId}")
    public @ResponseBody User showUser(@PathVariable String googleId) {
        System.setProperty("current.user.id", googleId);
        return userService.findByGoogleId(googleId);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("{id}")
    public User updateUsr(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") User user) {
        userService.deleteUser(user);
    }

    @GetMapping("info/{id}")
    public boolean getUserInfoStatus(@PathVariable("id") User user) {
        return userService.getUserInfoStatus(user);
    }

    @PostMapping("info/{id}")
    public User setUserInfoStatus(@PathVariable("id") User user) {
        return userService.setUserInfoStatus(user);
    }

}
