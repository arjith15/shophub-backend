package com.shophub.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {
    @NotBlank public String message;
}
