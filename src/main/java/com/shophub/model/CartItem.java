package com.shophub.model;

import java.util.Map;

/**
 * A single line item in a shopping cart session.
 */
public class CartItem {

    private String itemKey;             // unique key = productId + variantHash
    private int productId;
    private String name;
    private String brand;
    private double price;
    private String image;
    private int qty;
    private Map<String, String> variants;  // e.g. { "size":"M", "color":"Black" }

    public CartItem() {}

    // ── Getters & Setters ────────────────────────────────────
    public String getItemKey()                         { return itemKey; }
    public void setItemKey(String itemKey)             { this.itemKey = itemKey; }

    public int getProductId()                          { return productId; }
    public void setProductId(int productId)            { this.productId = productId; }

    public String getName()                            { return name; }
    public void setName(String name)                   { this.name = name; }

    public String getBrand()                           { return brand; }
    public void setBrand(String brand)                 { this.brand = brand; }

    public double getPrice()                           { return price; }
    public void setPrice(double price)                 { this.price = price; }

    public String getImage()                           { return image; }
    public void setImage(String image)                 { this.image = image; }

    public int getQty()                                { return qty; }
    public void setQty(int qty)                        { this.qty = qty; }

    public Map<String, String> getVariants()                   { return variants; }
    public void setVariants(Map<String, String> variants)      { this.variants = variants; }

    /** Subtotal for this line item. */
    public double getSubtotal() {
        return price * qty;
    }
}
