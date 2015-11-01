package com.example.guelmis.ffap.models;

public class Comment {
    private String title;
    private String body;
    private String username;
    private Double rating;

    public Comment(String _title, String _body, String _username, Double _rating){
        title = _title;
        body = _body;
        username = _username;
        rating = _rating;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUsername() {
        return username;
    }

    public Double getRating() {
        return rating;
    }
}
