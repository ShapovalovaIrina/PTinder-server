package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User newUser, String userId) {
        User oldUser = userRepository.findByGoogleId(userId);
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

    public void deleteUser(String userId) {
        User forDeletion = userRepository.findByGoogleId(userId);
        userRepository.delete(forDeletion);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(User user) {
        return userRepository.findByGoogleId(user.getGoogleId());
    }

    public boolean getUserInfoStatus(String userId) {
        User currentUser = userRepository.findByGoogleId(userId);
        return currentUser.isContactInfoPublic();
    }

    public User setUserInfoStatus(String userId) {
        User currentUser = userRepository.findByGoogleId(userId);
        currentUser.setContactInfoPublic(!currentUser.isContactInfoPublic());
        return userRepository.save(currentUser);
    }
    
}
