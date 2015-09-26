package com.example.guelmis.ffap.models;

public class LineItem extends Product {
    private int quantity;

    private Stock selectedStock;

    public LineItem(Product input, Stock selStk){
        title = input.title;
        brand = input.brand;
        model = input.model;
        desc = input.title + " " + input.brand + " " + input.model +" "+ input.year;
        imageurl = input.imageurl;
        year = input.year;
    //    price = input.price;
        id = input.id;

        quantity = 1;
        selectedStock = selStk;
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

    public Stock getSelectedStock() {
        return selectedStock;
    }

    public void setSelectedStock(Stock selectedStock) {
        this.selectedStock = selectedStock;
    }
}