package com.metodosestaticos;

public class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

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

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    double sum = 0.0;
    public double calculateTotal() {
        sum += price * quantity;
        return sum;
    }

    @Override
    public String toString() {
        return "Nome: " + name + ", Preço: " + price + ", Quantidade: " + quantity + ", valor total: " + sum;
    }
}
