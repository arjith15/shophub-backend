package com.shophub.model;

/**
 * Represents a product category displayed in the ShopHub navigation bar.
 */
public class Category {

    private String id;
    private String label;
    private String icon;

    public Category() {}

    public Category(String id, String label, String icon) {
        this.id    = id;
        this.label = label;
        this.icon  = icon;
    }

    public String getId()              { return id; }
    public void setId(String id)      { this.id = id; }

    public String getLabel()           { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getIcon()            { return icon; }
    public void setIcon(String icon)   { this.icon = icon; }
}
