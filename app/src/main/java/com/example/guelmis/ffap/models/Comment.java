package com.example.guelmis.ffap.models;

/**
 * Created by mario on 09/19/15.
 */
public class Comment {
    private String title;
    private String body;
    private String username;

    public Comment(String _title, String _body, String _username){
        title = _title;
        body = _body;
        username = _username;
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
}
