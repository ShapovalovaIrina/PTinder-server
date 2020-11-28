package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User newUser, Long userId) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setContactInfoPublic(newUser.isContactInfoPublic());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setMiddleName(newUser.getMiddleName());
        oldUser.setAddress(newUser.getAddress());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setGender(newUser.getGender());
        oldUser.setNumber(newUser.getNumber());
        return userRepository.save(oldUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUser(User user) {
        return userRepository.findById(user.getUserId());
    }

    public boolean getUserInfoStatus(User user) {
        Optional<User> currentUser = userRepository.findById(user.getUserId());
        return currentUser.map(User::isContactInfoPublic).orElse(false);
    }

    public User setUserInfoStatus(User user) {
        Optional<User> currentUser = userRepository.findById(user.getUserId());
        if (currentUser.isPresent()) {
            currentUser.get().setContactInfoPublic(!currentUser.get().isContactInfoPublic());
            return userRepository.save(currentUser.get());
        }
        return null;
    }

    public User findByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

}
