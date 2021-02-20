package com.trkpo.ptinder.entity;

public class Feed {
    private String author;
    private String score;
    private String title;
    private String content;

    public Feed(String author, String score, String title, String content) {
        this.author = author;
        this.score = score;
        this.title = title;
        this.content = content;
    }
}
