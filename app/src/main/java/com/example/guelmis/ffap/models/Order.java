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
    private boolean delivered;
    private boolean confirmed;

    public Order(int _id, String _address, String _invoice, int did, boolean _delivered, boolean _confirmed, String _createdat, ArrayList<LineItem> items){
        id = _id;
        address = _address;
        invoice = _invoice;
        createdAt = _createdat;
        location = null;
        delivery_id = did;
        delivered =_delivered;
        confirmed = _confirmed;
        lineItems = items;
    }

    public Order(int _id, LatLng _loca, String _invoice, int did, boolean _delivered, boolean _confirmed, String _createdat, ArrayList<LineItem> items){
        id = _id;
        address = null;
        invoice = _invoice;
        createdAt = _createdat;
        location= _loca;
        delivery_id = did;
        confirmed = _confirmed;
        delivered =_delivered;
        lineItems = items;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean wasDelivered() {
        return delivered;
    }

    public boolean isConfirmed() {
        return confirmed;
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
