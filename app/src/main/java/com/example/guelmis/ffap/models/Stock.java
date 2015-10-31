package com.example.guelmis.ffap.models;

public class Stock {
    int id;
    double price;
    int quantity;
    String sellerName;
    int sellerId;

    public Stock(int _id, double _price, int _quantity, String _sellerName, int _sellerId){
        id = _id;
        price = _price;
        quantity = _quantity;
        sellerName = _sellerName;
        sellerId = _sellerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
