package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationsService {
    private final UserRepository userRepository;


    public NotificationsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<Notifications> getNotificationsForUser(String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        return user.getNotifications();
    }
}
