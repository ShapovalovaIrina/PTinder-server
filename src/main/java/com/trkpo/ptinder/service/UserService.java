package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
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
        log.info("Going to delete user " + userId);
        User forDeletion = userRepository.findByGoogleId(userId);
        userRepository.delete(forDeletion);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(String googleId) {
        return userRepository.findByGoogleId(googleId);
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

    public boolean isCurrentUserExist(String googleId) {
        return userRepository.existsByGoogleId(googleId);
    }

    public void subscribe(String userId, GoogleId aimUser) {
        User currentUser = userRepository.findByGoogleId(userId);
        User userForSubscription = userRepository.findByGoogleId(aimUser.getGoogleId());
        Set<User> supscriptions = currentUser.getSubscriptions();
        supscriptions.add(userForSubscription);
        currentUser.setSubscriptions(supscriptions);
        userRepository.save(currentUser);
        userRepository.save(userForSubscription);
        log.info("User {} successfully subscribed on user {}", userId, aimUser);
    }


    public void unsubscribe(String userId, String aimUser) {
        User currentUser = userRepository.findByGoogleId(userId);
        User userForSubscription = userRepository.findByGoogleId(aimUser);
        currentUser.getSubscriptions().remove(userForSubscription);
        userRepository.save(currentUser);
        userForSubscription.getSubscribers().remove(currentUser);
        userRepository.save(userForSubscription);
        log.info("User {} successfully unsubscribed from user {}", userId, aimUser);
    }

    public boolean checkSubscription(String userId, String aimUser) {
        User currentUser = userRepository.findByGoogleId(userId);
        User userForSubscription = userRepository.findByGoogleId(aimUser);
        return currentUser.getSubscriptions().contains(userForSubscription);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
