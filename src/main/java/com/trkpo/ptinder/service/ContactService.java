package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.enums.NotificationType;
import com.trkpo.ptinder.repository.NotificationsRepository;
import com.trkpo.ptinder.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContactService {
    private final UserRepository userRepository;
    private final NotificationsRepository notificationsRepository;

    public ContactService(UserRepository userRepository, NotificationsRepository notificationsRepository) {
        this.userRepository = userRepository;
        this.notificationsRepository = notificationsRepository;
    }


    public void requestUserContact(String sourceGoogleId, String destinationGoogleId) {
        User sourceUser = userRepository.findByGoogleId(sourceGoogleId);
        User destinationUser = userRepository.findByGoogleId(destinationGoogleId);

        Notifications notification = new Notifications();
        notification.setType(NotificationType.CONTACT_INFO_REQUEST);
        notification.setAddressee(destinationUser);
        notification.setAddresseeFromId(sourceGoogleId);
        notification.setRead(false);
        notification.setText("Пользователь " + sourceUser.getFirstName() + " " + sourceUser.getLastName() +
                " запрашивает информацию о ваших личных данных для связи. Разрешить?");
        List<Notifications> notifications = destinationUser.getNotifications();
        notifications.add(notification);
        destinationUser.setNotifications(notifications);
        userRepository.save(destinationUser);
        userRepository.save(sourceUser);
        log.info("User {} successfully request contact for user {}", sourceUser.getGoogleId(), destinationUser.getGoogleId());
    }

    public void responseUserContact(String sourceGoogleId, String destinationGoogleId, String notificationId) {
        User sourceUser = userRepository.findByGoogleId(sourceGoogleId);
        User destinationUser = userRepository.findByGoogleId(destinationGoogleId);

        Notifications notification = new Notifications();
        notification.setType(NotificationType.CONTACT_INFO_ANSWER);
        notification.setAddressee(destinationUser);
        notification.setAddresseeFromId(sourceGoogleId);
        notification.setRead(false);
        notification.setText("Пользователь " + sourceUser.getFirstName() + " " + sourceUser.getLastName() +
                " предоставил вам информацию о своих личных данных для связи.\n" +
                "Адрес электронной почты: " + sourceUser.getEmail() + "\n" +
                "Номер мобильного телефона: " + sourceUser.getNumber());
        List<Notifications> notifications = destinationUser.getNotifications();
        notifications.add(notification);
        destinationUser.setNotifications(notifications);
        userRepository.save(destinationUser);
        userRepository.save(sourceUser);
        log.info("User {} successfully answer contact for user {}", sourceUser.getGoogleId(), destinationUser.getGoogleId());

        Notifications notifForReading = notificationsRepository.findById(Long.parseLong(notificationId)).get();
        notifForReading.setRead(true);
        notificationsRepository.save(notifForReading);
        log.info("Notification {} was read", notificationId);
    }
}
