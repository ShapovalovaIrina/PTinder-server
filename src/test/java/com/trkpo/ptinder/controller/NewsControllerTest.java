package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Feed;
import com.trkpo.ptinder.service.NewsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.trkpo.ptinder.config.Constants.NEWS_PATH;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class NewsControllerTest {

    @Mock
    NewsService newsService;

    @InjectMocks
    NewsController newsController;

    private MockMvc mockMvc;

    String title = "Funny bat got caught";
    String content = "Batbatbatbat";

    @Before
    public void init() {
        Feed feed = new Feed("Batman", "121",title , content);
        Feed anotherFeed = new Feed("a", "11", "s", "test");
        when(newsService.getPosts()).thenReturn(Arrays.asList(feed, anotherFeed));

        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(newsController).build();
    }

    @Test
    public void testThatNewsAreLoadedAfterNewsControllerCall() throws Exception {
        mockMvc.perform(get("/" + NEWS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].author").value("Batman"))
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content))
                .andReturn();
    }

}
