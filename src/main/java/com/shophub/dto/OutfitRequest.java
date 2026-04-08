package com.shophub.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class OutfitRequest {
    @NotBlank public String bodyType;       // Slim | Athletic | Medium | Heavy
    public List<String> stylePrefs;         // e.g. ["Casual","Smart-Casual"]
}
