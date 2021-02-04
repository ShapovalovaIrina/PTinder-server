package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.repository.NotificationsRepository;
import com.trkpo.ptinder.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ContactServiceTest {
    UserRepository userRepository;
    NotificationsRepository notificationsRepository;
    ContactService contactService;

    User testUser;
    User anotherUser;
    Notifications not;

    final String FIRST_ID = "111";
    final String SECOND_ID = "222";

    @Before
    public void before() {
        userRepository = Mockito.mock(UserRepository.class);
        notificationsRepository = Mockito.mock(NotificationsRepository.class);
        contactService = new ContactService(userRepository, notificationsRepository);

        testUser = new User();
        testUser.setFirstName("fName");

        anotherUser = new User();
        anotherUser.setFirstName("aName");

        when(userRepository.findByGoogleId(FIRST_ID)).thenReturn(testUser);
        when(userRepository.findByGoogleId(SECOND_ID)).thenReturn(anotherUser);

        not = new Notifications();
        not.setText("message text");
        not.setRead(false);
        when(notificationsRepository.findById(11L)).thenReturn(java.util.Optional.of(not));
    }

    @Test
    public void testAbilityToRequestUserContact() {
        assertTrue(anotherUser.getNotifications().isEmpty());
        contactService.requestUserContact(FIRST_ID, SECOND_ID);
        verify(userRepository, times(2)).save(any());
        assertEquals(1, anotherUser.getNotifications().size());
    }

    @Test
    public void testAbilityToResponse() {
        contactService.responseUserContact(FIRST_ID, SECOND_ID, "11");
        verify(userRepository, times(2)).save(any());
        assertTrue(not.isRead());
    }

}