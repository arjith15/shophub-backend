package com.shophub.model;

import java.util.Map;

/**
 * A single product line within a placed Order.
 */
public class OrderItem {

    private int productId;
    private String name;
    private String brand;
    private double unitPrice;
    private int qty;
    private Map<String, String> variants;
    private String image;

    public OrderItem() {}

    public int getProductId()                          { return productId; }
    public void setProductId(int productId)            { this.productId = productId; }

    public String getName()                            { return name; }
    public void setName(String name)                   { this.name = name; }

    public String getBrand()                           { return brand; }
    public void setBrand(String brand)                 { this.brand = brand; }

    public double getUnitPrice()                       { return unitPrice; }
    public void setUnitPrice(double unitPrice)         { this.unitPrice = unitPrice; }

    public int getQty()                                { return qty; }
    public void setQty(int qty)                        { this.qty = qty; }

    public Map<String, String> getVariants()           { return variants; }
    public void setVariants(Map<String, String> v)     { this.variants = v; }

    public String getImage()                           { return image; }
    public void setImage(String image)                 { this.image = image; }

    public double getSubtotal()                        { return unitPrice * qty; }
}
