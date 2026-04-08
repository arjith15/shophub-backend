package com.shophub.dto;

public class PricePoint {
    private String label;
    private double price;

    public PricePoint(String label, double price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() { return label; }
    public double getPrice() { return price; }
}
