package com.shophub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateCartQtyRequest {
    @NotBlank public String sessionId;
    @NotBlank public String itemKey;
    @Min(1)   public int qty;
}
