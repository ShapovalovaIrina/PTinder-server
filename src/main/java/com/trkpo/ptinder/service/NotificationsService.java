package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.NotificationsRepository;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationsService {
    private final UserRepository userRepository;
    private final NotificationsRepository notificationsRepository;

    public NotificationsService(UserRepository userRepository, NotificationsRepository notificationsRepository) {
        this.userRepository = userRepository;
        this.notificationsRepository = notificationsRepository;
    }

    public List<Notifications> getNotificationsForUser(String googleId) {
        User user = userRepository.findByGoogleId(googleId);
        List<Notifications> allNotif = user.getNotifications();
        return allNotif.stream().filter(o -> !o.isRead()).collect(Collectors.toList());
    }

    public void markAsRead(String notificationId) {
        Notifications notifForReading = notificationsRepository.findById(Long.parseLong(notificationId)).get();
        notifForReading.setRead(true);
        notificationsRepository.save(notifForReading);
        log.info("Notification {} was read", notificationId);
    }
}
