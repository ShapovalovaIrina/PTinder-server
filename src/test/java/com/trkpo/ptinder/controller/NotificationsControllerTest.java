package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Notifications;
import com.trkpo.ptinder.service.NotificationsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.trkpo.ptinder.config.Constants.NOTIFICATIONS_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class NotificationsControllerTest {

    @Mock
    NotificationsService notificationsService;

    @InjectMocks
    NotificationsController notificationsController;

    private MockMvc mockMvc;
    Notifications first;

    @Before
    public void init() {
        first = new Notifications();
        first.setId(1L);
        first.setText("This is notification");
        Notifications second = new Notifications();
        Notifications third = new Notifications();

        when(notificationsService.getNotificationsForUser(any())).thenReturn(Arrays.asList(first, second, third));

        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(notificationsController).build();
    }

    @Test
    public void testCanGetNotificationListForUser() throws Exception {
        mockMvc.perform(get("/" + NOTIFICATIONS_PATH + "/{googleId}", "123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].text").value("This is notification"))
                .andExpect(jsonPath("$[1].text").doesNotExist())
                .andExpect(jsonPath("$[2].text").doesNotExist())
                .andReturn();
    }

    @Test
    public void testCanMarkNotificationAsRead() throws Exception {
        NotificationsService notificationsService = Mockito.mock(NotificationsService.class);
        NotificationsController notificationsController = new NotificationsController(notificationsService);
        MockMvc mockMvc = standaloneSetup(notificationsController).build();
        mockMvc.perform(post("/" + NOTIFICATIONS_PATH + "/{notificationId}", "123"))
                .andExpect(status().isOk());
        verify(notificationsService).markAsRead("123");

    }

}
