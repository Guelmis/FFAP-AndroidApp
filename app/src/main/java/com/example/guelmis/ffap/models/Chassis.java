package com.example.guelmis.ffap.models;

public class Chassis {
    private String chassis;
    private String username;

    public Chassis(String _chassis, String _username){
        chassis = _chassis;
        username = _username;
    }

    public String getChassis() {
        return chassis;
    }


    public String getUsername() {
        return username;
    }

}

