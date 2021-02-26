package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.templates.GoogleId;
import com.trkpo.ptinder.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.trkpo.ptinder.config.Constants.SUBSCRIPTION_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionControllerTest extends AbstractControllerTest {
    UserService userService;
    SubscriptionController subscriptionController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        userService = Mockito.mock(UserService.class);
        subscriptionController = new SubscriptionController(userService);

        mockMvc = standaloneSetup(subscriptionController).build();
    }

    @Test
    public void testCanSubscribeOnUser() throws Exception {
        GoogleId googleId = new GoogleId("222");
        googleId.setGoogleId("222");
        String r = getObjectAsJson(googleId);
        mockMvc.perform(post("/" + SUBSCRIPTION_PATH + "/{userId}", "111")
                .contentType(APPLICATION_JSON_UTF8)
                .content(r))
                .andExpect(status().isOk());
        verify(userService).subscribe(any(), any());
    }

    @Test
    public void testCanUnsubscribeFromUser() throws Exception {
        mockMvc.perform(delete("/" + SUBSCRIPTION_PATH + "/{userId}/{targetId}", "111", "222"))
                .andExpect(status().isOk());
        verify(userService).unsubscribe("111", "222");
    }

    @Test
    public void testCanCheckUserSubscriptions() throws Exception {
        mockMvc.perform(get("/" + SUBSCRIPTION_PATH + "/check/{userId}/{targetId}", "111", "222"))
                .andExpect(status().isOk());
        verify(userService).checkSubscription("111", "222");
    }
}
