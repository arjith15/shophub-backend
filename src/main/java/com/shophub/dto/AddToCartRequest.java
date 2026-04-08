package com.shophub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class AddToCartRequest {
    @NotBlank public String sessionId;
    @NotNull  public Integer productId;
    @Min(1)   public int qty = 1;
    public Map<String, String> variants;
}
