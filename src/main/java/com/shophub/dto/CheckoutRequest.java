package com.shophub.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckoutRequest {

    @NotBlank public String sessionId;

    // Customer
    @NotBlank public String customerName;
    @NotBlank public String customerEmail;
    @NotBlank public String customerPhone;

    // Address
    @NotBlank public String addressLine1;
    public String addressLine2;
    @NotBlank public String city;
    @NotBlank public String state;
    @NotBlank public String pincode;
    @NotBlank public String country;

    // Payment  —  "card" | "upi" | "cod"
    @NotBlank public String paymentMode;
    public String upiId;     // optional, only for UPI-ID flow
}
