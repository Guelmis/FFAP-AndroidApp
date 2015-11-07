package com.example.guelmis.ffap.models;

public class Vehicle {
    private int id;
    private String brand;
    private String model;
    private String year;
    private String vin;

    public Vehicle(String _brand, String _model, String _year, String _vin){
        brand = _brand;
        model = _model;
        year = _year;
        vin = _vin;
    }

    public Vehicle(int _id, String _brand, String _model, String _year, String _vin){
        id = _id;
        brand = _brand;
        model = _model;
        year = _year;
        vin = _vin;
    }

    public String getDescription(){
        return brand + " " + model + " " + year;
    }

    public String getVin(){
        return vin;
    }

    public  int getId(){
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }
}