package com.example.guelmis.ffap.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Seller {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String logo_url;
    private ArrayList<Comment> reviews;

    private LatLng location;

    public Seller(String _id, String _name, String _address, String _phone, String _logo_url,
                  ArrayList<Comment> _rev, LatLng _location){
        name = _name;
        address = _address;
        phone = _phone;
        logo_url = _logo_url;
        reviews = _rev;
        id = _id;
        location = _location;
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

    public float getAverageRating(){
        float sum =0;
        for(int i=0; i<reviews.size(); i++){
            sum += Double.valueOf(reviews.get(i).getRating());
        }
        return sum/reviews.size();
    }

    public String getID() {
        return id;
    }
    public LatLng getLocation() {
        return location;
    }

}
