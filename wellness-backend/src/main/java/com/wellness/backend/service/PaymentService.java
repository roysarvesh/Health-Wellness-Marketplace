package com.wellness.backend.service;

import com.wellness.backend.model.*;
import com.wellness.backend.notification.NotificationService;
import com.wellness.backend.repository.OrderRepository;
import com.wellness.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;
    private final NotificationService notificationService;

    public PaymentService(PaymentRepository paymentRepo,
                          OrderRepository orderRepo,
                          NotificationService notificationService) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
        this.notificationService = notificationService;
    }


    @Transactional
    public Payment initiatePayment(User user, Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized payment attempt");
        }

        paymentRepo.findByOrder(order).ifPresent(p -> {
            throw new RuntimeException("Payment already exists");
        });

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setPaymentId("MOCK_" + UUID.randomUUID());

        return paymentRepo.save(payment);
    }


    @Transactional
    public void confirmPayment(String paymentId) {

        Payment payment = paymentRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Invalid payment ID"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Payment already processed");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setConfirmedAt(LocalDateTime.now());

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PLACED);

        paymentRepo.save(payment);
        orderRepo.save(order);

        // 🔔 Notify user
        notificationService.notify(
                order.getUser().getId(),
                "PAYMENT_SUCCESS",
                "Your payment for Order #" + order.getId() + " was successful"
        );
    }


    @Transactional(readOnly = true)
    public List<Payment> paymentHistory(Long userId) {
        return paymentRepo.findByOrder_User_IdOrderByCreatedAtDesc(userId);
    }


    @Transactional
    public void requestRefund(Long userId, Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized refund request");
        }

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new RuntimeException("Refund not allowed for this order");
        }

        order.setStatus(OrderStatus.REFUND_REQUESTED);
        order.setLastAction("REFUND_REQUEST");
        order.setActionReason("Refund requested by user");

        orderRepo.save(order);

    }


    @Transactional
    public void refund(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentRepo.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Only successful payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setConfirmedAt(LocalDateTime.now());

        order.setStatus(OrderStatus.REFUNDED);
        order.setRefundedAt(LocalDateTime.now());
        order.setLastAction("REFUND");
        order.setActionReason("Refund approved by admin");

        paymentRepo.save(payment);
        orderRepo.save(order);

        // 🔔 Notify user
        notificationService.notify(
                order.getUser().getId(),
                "PAYMENT_REFUND",
                "Your payment for Order #" + order.getId() + " has been refunded"
        );
    }


    @Transactional(readOnly = true)
    public List<Payment> allPayments() {
        return paymentRepo.findAll();
    }
}
