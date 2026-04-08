package com.shophub.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Represents a ShopHub product.
 * Mirrors the structure in products.js
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    private String brand;
    private double price;

    @Column(length = 1000)
    private String image;

    // ── List fields stored as separate tables ─────────────────

    @ElementCollection
    @CollectionTable(name = "product_sizes",   joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    private List<String> sizes;

    @ElementCollection
    @CollectionTable(name = "product_colors",  joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "color")
    private List<String> colors;

    @ElementCollection
    @CollectionTable(name = "product_storage", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "storage")
    private List<String> storage;

    @ElementCollection
    @CollectionTable(name = "product_ram",     joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "ram")
    private List<String> ram;

    // ── Specs stored as key-value table ───────────────────────

    @ElementCollection
    @CollectionTable(name = "product_specs",   joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "spec_key")
    @Column(name = "spec_value", length = 500)
    private Map<String, String> specs;

    // ── Constructors ──────────────────────────────────────────

    public Product() {}

    public Product(int id, String name, String category, String brand,
                   double price, String image) {
        this.id       = id;
        this.name     = name;
        this.category = category;
        this.brand    = brand;
        this.price    = price;
        this.image    = image;
    }

    // ── Getters & Setters ─────────────────────────────────────
	
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public String getName()                         { return name; }
    public void setName(String name)                { this.name = name; }

    public String getCategory()                     { return category; }
    public void setCategory(String category)        { this.category = category; }

    public String getBrand()                        { return brand; }
    public void setBrand(String brand)              { this.brand = brand; }

    public double getPrice()                        { return price; }
    public void setPrice(double price)              { this.price = price; }

    public String getImage()                        { return image; }
    public void setImage(String image)              { this.image = image; }

    public List<String> getSizes()                  { return sizes; }
    public void setSizes(List<String> sizes)        { this.sizes = sizes; }

    public List<String> getColors()                 { return colors; }
    public void setColors(List<String> colors)      { this.colors = colors; }

    public List<String> getStorage()                { return storage; }
    public void setStorage(List<String> storage)    { this.storage = storage; }

    public List<String> getRam()                    { return ram; }
    public void setRam(List<String> ram)            { this.ram = ram; }

    public Map<String, String> getSpecs()           { return specs; }
    public void setSpecs(Map<String, String> specs) { this.specs = specs; }
}