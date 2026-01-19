package com.wellness.backend.notification;

import com.wellness.backend.model.Notification;
import com.wellness.backend.model.User;
import com.wellness.backend.repository.NotificationRepository;
import com.wellness.backend.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final UserRepository userRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(
            NotificationRepository repo,
            UserRepository userRepo,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.messagingTemplate = messagingTemplate;
    }


    public void notify(Long userId, String type, String message) {

        Notification n = new Notification();
        n.setUserId(userId);
        n.setType(type);
        n.setMessage(message);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());

        repo.save(n);

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + userId,
                message
        );
    }

    public void notifyUser(Long userId, String message) {
        notify(userId, "SYSTEM", message);
    }


    public List<Notification> getUserNotifications(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Long unreadCount(Long userId) {
        return repo.countByUserIdAndIsReadFalse(userId);
    }


    @Transactional
    public void markAsRead(Long notificationId, Long userId) {

        Notification n = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!n.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (!n.isRead()) {
            n.setRead(true);
            repo.save(n);
        }
    }


    @Transactional
    public void broadcast(String message) {

        List<User> users = userRepo.findAll();

        for (User user : users) {

            Notification n = new Notification();
            n.setUserId(user.getId());
            n.setType("ADMIN_BROADCAST");
            n.setMessage(message);
            n.setRead(false);
            n.setCreatedAt(LocalDateTime.now());

            repo.save(n);

            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + user.getId(),
                    message
            );
        }
    }
}
