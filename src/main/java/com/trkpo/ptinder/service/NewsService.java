package com.trkpo.ptinder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trkpo.ptinder.entity.Feed;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class NewsService {

    private String getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("kuN0N-lXa7YVnA", "9oTtJBlmkpbriQyQHClc1OVYX64sBQ");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.put("User-Agent",
                Collections.singletonList("tomcat:com.PTinder-server:v1.0-SNAPSHOT (by /u/Friendly_Surround531)"));
        String body = "grant_type=client_credentials";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        String authUrl = "https://www.reddit.com/api/v1/access_token";
        ResponseEntity<String> response = restTemplate.postForEntity(
                authUrl, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map.putAll(mapper
                    .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.getBody());
        return String.valueOf(map.get("access_token"));
    }

    private String readArticles(String subReddit) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String authToken = getAuthToken();
        headers.setBearerAuth(authToken);
        headers.put("User-Agent",
                Collections.singletonList("tomcat:com.PTinder-server:v1.0-SNAPSHOT (by /u/Friendly_Surround531)"));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        String url = "https://oauth.reddit.com/r/" + subReddit + "/hot";
        ResponseEntity<String> response
                = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    private List<Feed> getNewsFromJSON(String response) {
        List<Feed> collectedFeed = new ArrayList<>();
        try {
            Object jObj = new JSONParser().parse(response);
            JSONObject jo = (JSONObject) jObj;

            JSONObject data = (JSONObject) jo.get("data");
            JSONArray children = (JSONArray) data.get("children");
            for (int i = 0; i < 15; i++) {
                JSONObject child = (JSONObject) children.get(i);
                JSONObject topic = (JSONObject) child.get("data");
                String author = (String) topic.get("author");
                String imageUrl = (String) topic.get("thumbnail");
                Long rScore = (Long) topic.get("score");
                String title = (String) topic.get("title");
                collectedFeed.add(new Feed(author, rScore.toString(), title, imageUrl));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return collectedFeed;
    }

    public List<Feed> getPosts() {
        return getNewsFromJSON(readArticles("aww"));
    }
}
