package com.wellness.backend.controller;

import com.wellness.backend.dto.ChatMessageRequest;
import com.wellness.backend.model.ChatMessage;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.ChatService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService,
                          SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public ChatMessage sendChatMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChatMessageRequest request
    ) {
        Long senderId = userDetails.getUser().getId();

        // 1️⃣ Save message + trigger notification
        ChatMessage saved = chatService.saveMessage(
                senderId,
                request.getReceiverId(),
                request.getContent()
        );

        // 2️⃣ Push chat message via WebSocket
        messagingTemplate.convertAndSendToUser(
                request.getReceiverId().toString(),
                "/queue/messages",
                saved
        );

        return saved;
    }
}
