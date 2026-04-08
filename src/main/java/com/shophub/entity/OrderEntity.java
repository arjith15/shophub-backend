package com.shophub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity — maps to the `orders` table in MySQL.
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "order_id", length = 20)
    private String orderId;

    @Column(name = "placed_at")
    private LocalDateTime placedAt;

    @Column(name = "status", length = 20)
    private String status;

    // Customer
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    // Address
    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "country")
    private String country;

    // Payment
    @Column(name = "payment_mode", length = 10)
    private String paymentMode;

    @Column(name = "payment_confirmed")
    private boolean paymentConfirmed;

    // Total
    @Column(name = "total")
    private double total;

    // Items stored as JSON string
    @Column(name = "items_json", columnDefinition = "TEXT")
    private String itemsJson;

    public OrderEntity() {
        this.placedAt = LocalDateTime.now();
        this.status   = "CONFIRMED";
    }

    // ── Getters & Setters ────────────────────────────────────
    public String getOrderId()                           { return orderId; }
    public void setOrderId(String orderId)               { this.orderId = orderId; }

    public LocalDateTime getPlacedAt()                   { return placedAt; }
    public void setPlacedAt(LocalDateTime placedAt)      { this.placedAt = placedAt; }

    public String getStatus()                            { return status; }
    public void setStatus(String status)                 { this.status = status; }

    public String getCustomerName()                      { return customerName; }
    public void setCustomerName(String customerName)     { this.customerName = customerName; }

    public String getCustomerEmail()                     { return customerEmail; }
    public void setCustomerEmail(String customerEmail)   { this.customerEmail = customerEmail; }

    public String getCustomerPhone()                     { return customerPhone; }
    public void setCustomerPhone(String customerPhone)   { this.customerPhone = customerPhone; }

    public String getAddressLine1()                      { return addressLine1; }
    public void setAddressLine1(String addressLine1)     { this.addressLine1 = addressLine1; }

    public String getAddressLine2()                      { return addressLine2; }
    public void setAddressLine2(String addressLine2)     { this.addressLine2 = addressLine2; }

    public String getCity()                              { return city; }
    public void setCity(String city)                     { this.city = city; }

    public String getState()                             { return state; }
    public void setState(String state)                   { this.state = state; }

    public String getPincode()                           { return pincode; }
    public void setPincode(String pincode)               { this.pincode = pincode; }

    public String getCountry()                           { return country; }
    public void setCountry(String country)               { this.country = country; }

    public String getPaymentMode()                       { return paymentMode; }
    public void setPaymentMode(String paymentMode)       { this.paymentMode = paymentMode; }

    public boolean isPaymentConfirmed()                  { return paymentConfirmed; }
    public void setPaymentConfirmed(boolean v)           { this.paymentConfirmed = v; }

    public double getTotal()                             { return total; }
    public void setTotal(double total)                   { this.total = total; }

    public String getItemsJson()                         { return itemsJson; }
    public void setItemsJson(String itemsJson)           { this.itemsJson = itemsJson; }
}
