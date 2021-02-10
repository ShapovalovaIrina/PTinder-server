package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.repository.NotificationsRepository;
import com.trkpo.ptinder.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class NotificationsServiceTest extends AbstractServiceTest {
    UserRepository userRepository;
    NotificationsRepository notificationsRepository;

    NotificationsService notificationsService;

    @Before
    public void init() {
        initPetAndUser();
        userRepository = Mockito.mock(UserRepository.class);
        notificationsRepository = Mockito.mock(NotificationsRepository.class);

        notificationsService = new NotificationsService(userRepository, notificationsRepository);
        when(userRepository.findByGoogleId(TEST_GOOGLE_ID)).thenReturn(testUser);

        List<Notifications> allNotifications = new ArrayList<>();
        Notifications first = new Notifications();
        first.setText("first");
        Notifications second = new Notifications();
        second.setRead(true);
        second.setText("second");
        Notifications third = new Notifications();
        third.setText("third");
        third.setId(3L);
        allNotifications.add(first);
        allNotifications.add(second);
        allNotifications.add(third);

        testUser.setNotifications(allNotifications);
        when(notificationsRepository.findById(3L)).thenReturn(java.util.Optional.of(third));
    }

    @Test
    public void testCanGetAllUsersNotifications() {
        assertEquals(2, notificationsService.getNotificationsForUser(TEST_GOOGLE_ID).size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testExceptionIsThrownWhenThereAreNoSuchNotification() {
        notificationsService.markAsRead("100");
    }

    @Test
    public void testCanReadNotification() {
        notificationsService.markAsRead("3");

        assertEquals(1, notificationsService.getNotificationsForUser(TEST_GOOGLE_ID).size());
        assertFalse(notificationsService.getNotificationsForUser(TEST_GOOGLE_ID).toString().contains("third"));
    }

}