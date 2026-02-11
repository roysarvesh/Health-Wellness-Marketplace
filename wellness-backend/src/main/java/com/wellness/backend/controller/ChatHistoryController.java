package com.wellness.backend.controller;

import com.wellness.backend.model.ChatMessage;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    private final ChatService service;

    public ChatHistoryController(ChatService service) {
        this.service = service;
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ChatMessage>> history(
            @AuthenticationPrincipal CustomUserDetails me,
            @PathVariable Long userId
    ) {
        // 🔐 If JWT is missing or invalid
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long myId = me.getUser().getId();

        // 🛡 Optional safety: prevent fetching own history with invalid user
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<ChatMessage> messages = service.getChatHistory(myId, userId);
        return ResponseEntity.ok(messages);
    }
}
