package com.trkpo.ptinder.service;

import com.trkpo.ptinder.entity.User;
import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest extends AbstractServiceTest {
    UserRepository userRepository;
    UserService userService;

    @Before
    public void init() {
        initPetAndUser();
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);

        testUser.setContactInfoPublic(false);
        testUser.setSubscriptions(new HashSet<>());

        when(userRepository.findByGoogleId(TEST_GOOGLE_ID)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, new User(), new User()));
        when(userRepository.existsByGoogleId(TEST_GOOGLE_ID)).thenReturn(true);
    }

    @Test
    public void testGetUserByGoogleIdReturnsProperUser() {
        assertEquals(testUser.getFirstName(), userRepository.findByGoogleId(TEST_GOOGLE_ID).getFirstName());
    }

    @Test
    public void testUserSavingToDatabase() {
        assertEquals(testUser, userService.addUser(testUser));
        verify(userRepository).save(testUser);
    }

    @Test
    public void testUserIsUpdated() {
        User newUser = new User();
        String newName = "newName";
        newUser.setFirstName(newName);

        userService.updateUser(newUser, TEST_GOOGLE_ID);
        verify(userRepository).save(any());
    }

    @Test
    public void testUserDeletion() {
        userService.deleteUser(TEST_GOOGLE_ID);
        verify(userRepository).delete(any());
    }

    @Test
    public void testCanFindAllUsers() {
        assertEquals(3, userService.getAllUsers().size());
        assertTrue(userService.getAllUsers().contains(testUser));
    }

    @Test
    public void testCheckInfoStatusGetter() {
        assertFalse(userService.getUserInfoStatus(TEST_GOOGLE_ID));
    }

    @Test
    public void testCanSetUserInfoStatus() {
        userService.setUserInfoStatus(TEST_GOOGLE_ID);
        verify(userRepository).save(any());
    }

    @Test
    public void testUserExists() {
        assertTrue(userService.isCurrentUserExist(TEST_GOOGLE_ID));
    }

    @Test
    public void testThatUserCanBeFound() {
        assertEquals(testUser, userRepository.findByGoogleId(TEST_GOOGLE_ID));
    }

    @Test
    public void testAbilityToSubscribe() {
        User userForSubscription = new User();
        userForSubscription.setFirstName("anotherUser");
        String userForSubscriptionId = "222";
        userForSubscription.setGoogleId(userForSubscriptionId);
        Set<User> subscriptions = new HashSet<>();
        subscriptions.add(userForSubscription);
        when(userRepository.findByGoogleId(userForSubscriptionId)).thenReturn(userForSubscription);

        Set<User> subscribers = new HashSet<>();
        subscribers.add(testUser);
        userForSubscription.setSubscribers(subscribers);

        userService.subscribe(TEST_GOOGLE_ID, new GoogleId(userForSubscriptionId));
        verify(userRepository, times(2)).save(any());

        assertEquals(subscriptions, testUser.getSubscriptions());
        assertEquals(subscribers, userForSubscription.getSubscribers());
    }

    @Test
    public void testAbilityToUnsubscribe() {
        User userForSubscription = new User();
        userForSubscription.setFirstName("anotherUser");
        String userForSubscriptionId = "222";
        userForSubscription.setGoogleId(userForSubscriptionId);
        Set<User> subscriptions = new HashSet<>();
        subscriptions.add(userForSubscription);
        when(userRepository.findByGoogleId(userForSubscriptionId)).thenReturn(userForSubscription);
        testUser.setSubscriptions(subscriptions);
        Set<User> subscribers = new HashSet<>();
        subscribers.add(testUser);
        userForSubscription.setSubscribers(subscribers);

        userService.unsubscribe(TEST_GOOGLE_ID, userForSubscriptionId);
        verify(userRepository, times(2)).save(any());
        assertEquals(0, testUser.getSubscriptions().size());
        assertEquals(0, userForSubscription.getSubscribers().size());
    }

    @Test
    public void testCheckSubscriptions() {
        User userForSubscription = new User();
        userForSubscription.setFirstName("anotherUser");
        String userForSubscriptionId = "222";
        userForSubscription.setGoogleId(userForSubscriptionId);
        Set<User> subscriptions = new HashSet<>();
        subscriptions.add(userForSubscription);
        when(userRepository.findByGoogleId(userForSubscriptionId)).thenReturn(userForSubscription);
        testUser.setSubscriptions(subscriptions);
        Set<User> subscribers = new HashSet<>();
        subscribers.add(testUser);
        userForSubscription.setSubscribers(subscribers);

        assertTrue(userService.checkSubscription(TEST_GOOGLE_ID, userForSubscriptionId));
    }

    @Test
    public void testFindUser() {
        assertEquals(testUser, userService.findUser(TEST_GOOGLE_ID));
    }
}