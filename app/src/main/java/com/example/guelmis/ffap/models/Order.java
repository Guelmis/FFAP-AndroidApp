package com.example.guelmis.ffap.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mario on 11/01/15.
 */
public class Order {
    private int id;
    private String invoice;
    private String createdAt;
    private LatLng location;
    private String address;

    public Order(int _id, String _address, String _invoice, String _createdat){
        id = _id;
        address = _address;
        invoice = _invoice;
        createdAt = _createdat;
        location = null;
    }

    public Order(int _id, LatLng _loca, String _invoice, String _createdat){
        id = _id;
        address = null;
        invoice = _invoice;
        createdAt = _createdat;
        location= _loca;
    }

    public String getInvoice() {
        return invoice;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public LatLng getLocation() {
        return location;
    }


}
