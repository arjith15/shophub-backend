package com.shophub.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * A placed order in the ShopHub system.
 */
public class Order {

    public enum Status { PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED }
    public enum PaymentMode { CARD, UPI, COD }

    private String orderId;
    private LocalDateTime placedAt;
    private Status status;

    // Customer info
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // Delivery address
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private String country;

    // Payment
    private PaymentMode paymentMode;
    private boolean paymentConfirmed;

    // Items & totals
    private List<OrderItem> items;
    private double total;

    public Order() {
        this.placedAt = LocalDateTime.now();
        this.status   = Status.PENDING;
    }

    // ── Getters & Setters ────────────────────────────────────
    public String getOrderId()                           { return orderId; }
    public void setOrderId(String orderId)               { this.orderId = orderId; }

    public LocalDateTime getPlacedAt()                   { return placedAt; }
    public void setPlacedAt(LocalDateTime placedAt)      { this.placedAt = placedAt; }

    public Status getStatus()                            { return status; }
    public void setStatus(Status status)                 { this.status = status; }

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

    public PaymentMode getPaymentMode()                      { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode)      { this.paymentMode = paymentMode; }

    public boolean isPaymentConfirmed()                      { return paymentConfirmed; }
    public void setPaymentConfirmed(boolean paymentConfirmed){ this.paymentConfirmed = paymentConfirmed; }

    public List<OrderItem> getItems()                    { return items; }
    public void setItems(List<OrderItem> items)          { this.items = items; }

    public double getTotal()                             { return total; }
    public void setTotal(double total)                   { this.total = total; }
}
