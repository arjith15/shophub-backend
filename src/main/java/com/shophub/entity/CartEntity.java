package com.shophub.entity;

import jakarta.persistence.*;

/**
 * JPA Entity — maps to the `cart_items` table in MySQL.
 */
@Entity
@Table(name = "cart_items")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "item_key", nullable = false)
    private String itemKey;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "price")
    private double price;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "qty")
    private int qty;

    @Column(name = "variants_json")
    private String variantsJson;  // stored as JSON string e.g. {"color":"Blue","size":"M"}

    public CartEntity() {}

    // ── Getters & Setters ────────────────────────────────────
    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getSessionId()                 { return sessionId; }
    public void setSessionId(String sessionId)   { this.sessionId = sessionId; }

    public String getItemKey()                   { return itemKey; }
    public void setItemKey(String itemKey)       { this.itemKey = itemKey; }

    public int getProductId()                    { return productId; }
    public void setProductId(int productId)      { this.productId = productId; }

    public String getName()                      { return name; }
    public void setName(String name)             { this.name = name; }

    public String getBrand()                     { return brand; }
    public void setBrand(String brand)           { this.brand = brand; }

    public double getPrice()                     { return price; }
    public void setPrice(double price)           { this.price = price; }

    public String getImage()                     { return image; }
    public void setImage(String image)           { this.image = image; }

    public int getQty()                          { return qty; }
    public void setQty(int qty)                  { this.qty = qty; }

    public String getVariantsJson()              { return variantsJson; }
    public void setVariantsJson(String v)        { this.variantsJson = v; }
}
