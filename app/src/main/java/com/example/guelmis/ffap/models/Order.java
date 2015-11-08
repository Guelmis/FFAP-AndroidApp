package com.example.guelmis.ffap.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by mario on 11/01/15.
 */
public class Order {
    private int id;
    private int delivery_id;
    private String invoice;
    private String createdAt;
    private LatLng location;
    private String address;
    private ArrayList<LineItem> lineItems;

    public Order(int _id, String _address, String _invoice, int did, String _createdat, ArrayList<LineItem> items){
        id = _id;
        address = _address;
        invoice = _invoice;
        createdAt = _createdat;
        location = null;
        delivery_id = did;
        lineItems = items;
    }

    public Order(int _id, LatLng _loca, String _invoice, int did, String _createdat, ArrayList<LineItem> items){
        id = _id;
        address = null;
        invoice = _invoice;
        createdAt = _createdat;
        location= _loca;
        delivery_id = did;
        lineItems = items;
    }

    public int getDelivery_id() {
        return delivery_id;
    }

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
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
        String ret = createdAt.split("T")[0];
        return ret;
    }

    public String getTime(){
        return createdAt.split("T")[1].split("\\.")[0];
    }

    public LatLng getLocation() {
        return location;
    }


}
