package com.wellness.backend.service;

import com.wellness.backend.model.ChatMessage;
import com.wellness.backend.model.MessageStatus;
import com.wellness.backend.notification.NotificationService;
import com.wellness.backend.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository repo;
    private final NotificationService notificationService;

    public ChatService(ChatMessageRepository repo,
                       NotificationService notificationService) {
        this.repo = repo;
        this.notificationService = notificationService;
    }

    public ChatMessage saveMessage(Long senderId, Long receiverId, String content) {

        // 💾 Save chat message
        ChatMessage msg = new ChatMessage();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setStatus(MessageStatus.SENT);
        msg.setTimestamp(LocalDateTime.now());

        ChatMessage saved = repo.save(msg);

        // 🔔 NEW: Save DB notification + WebSocket push
        notificationService.notifyChatMessage(
                receiverId,
                senderId,
                content
        );

        return saved;
    }

    public List<ChatMessage> getChatHistory(Long user1, Long user2) {
        return repo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                user1, user2,
                user1, user2
        );
    }
}
