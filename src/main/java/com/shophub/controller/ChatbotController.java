package com.shophub.controller;

import com.shophub.dto.ApiResponse;
import com.shophub.dto.ChatRequest;
import com.shophub.service.ChatbotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Chatbot REST API
 *
 * POST /api/chatbot/message    — send a product query, receive usage instructions
 */
@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    /**
     * Accept a user message and return the product usage response.
     *
     * Request body:  { "message": "how do I use my yoga mat?" }
     * Response:      { "success": true, "data": { "reply": "..." } }
     */
    @PostMapping("/message")
    public ResponseEntity<ApiResponse<Map<String, String>>> message(
            @Valid @RequestBody ChatRequest req) {

        String reply = chatbotService.getResponse(req.message);
        return ResponseEntity.ok(
            ApiResponse.ok(Map.of(
                "query", req.message,
                "reply", reply
            ))
        );
    }
}
