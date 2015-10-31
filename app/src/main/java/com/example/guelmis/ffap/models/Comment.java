package com.example.guelmis.ffap.models;

public class Comment {
    private String title;
    private String body;
    private String username;
    private String rating;

    public Comment(String _title, String _body, String _username, String _rating){
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

    public String getRating() {
        return rating;
    }
}
