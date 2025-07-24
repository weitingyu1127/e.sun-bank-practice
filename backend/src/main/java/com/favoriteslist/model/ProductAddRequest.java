package com.favoriteslist.model;

public class ProductAddRequest {
    private String name;
    private int price;
    private double fee;

    public ProductAddRequest() {}

    public ProductAddRequest(String name, int price, double fee) {
        this.name = name;
        this.price = price;
        this.fee = fee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
