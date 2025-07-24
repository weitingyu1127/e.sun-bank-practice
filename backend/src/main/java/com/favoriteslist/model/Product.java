package com.favoriteslist.model;

public class Product {
    private String name;
    private double price;
    private double fee;

    // 無參數建構子
    public Product() {
    }

    // 有參數建構子
    public Product(String name, double price, double fee) {
        this.name = name;
        this.price = price;
        this.fee = fee;
    }

    // Getter 和 Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
