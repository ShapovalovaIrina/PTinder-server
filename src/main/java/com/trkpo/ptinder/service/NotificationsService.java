package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.NotificationId;
import com.trkpo.ptinder.entity.templates.NotificationTemplate;
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

    public void markAsRead(String googleId, NotificationTemplate notifs) {
        System.out.println(notifs.getList());
        User user = userRepository.findByGoogleId(googleId);
        List<NotificationId> readNotifications = notifs.getList();
        List<Notifications> allNotifications = user.getNotifications();
        for (Notifications n : allNotifications) {
            for (NotificationId r : readNotifications) {
                if (n.getId().toString().equals(r.getId())) {
                    n.setRead(true);
                    notificationsRepository.save(n);
                    log.info("Notification {} was read", n);
                }
            }
        }
        user.setNotifications(allNotifications);
        userRepository.save(user);
    }
}
