package com.example.guelmis.ffap.models;

import java.util.ArrayList;

/**
 * Created by mario on 09/27/15.
 */
public class Seller {
    private String name;
    private String address;
    private String phone;
    private String logo_url;
    private ArrayList<Comment> reviews;

    public Seller(String _name, String _address, String _phone, String _logo_url, ArrayList<Comment> _rev){
        name = _name;
        address = _address;
        phone = _phone;
        logo_url = _logo_url;
        reviews = _rev;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public ArrayList<Comment> getReviews() {
        return reviews;
    }

}
