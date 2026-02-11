package com.wellness.backend.repository;

import com.wellness.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            Long sender1, Long receiver1,
            Long sender2, Long receiver2
    );
}
