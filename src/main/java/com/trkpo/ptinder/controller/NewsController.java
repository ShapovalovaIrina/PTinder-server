package com.trkpo.ptinder.controller;

import com.trkpo.ptinder.entity.Feed;
import com.trkpo.ptinder.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.trkpo.ptinder.config.Constants.NEWS_PATH;

@RestController
@RequestMapping(NEWS_PATH)
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<Feed> getPostsFromReddit() {
        return newsService.getPosts();
    }
}
