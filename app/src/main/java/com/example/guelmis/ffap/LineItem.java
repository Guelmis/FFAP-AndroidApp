package com.example.guelmis.ffap;

public class LineItem extends Product {
    private int quantity;

    LineItem(Product input){
        title = input.title;
        brand = input.brand;
        model = input.model;
        desc = input.title + " " + input.brand + " " + input.model +" "+ input.year;
        imageurl = input.imageurl;
        year = input.year;
        price = input.price;
        id = input.id;

        quantity = 1;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addOne(){
        this.quantity += 1;
    }
}