package com.wellness.backend.dto;

import com.wellness.backend.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdminPaymentResponse {

    private String paymentId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

    public AdminPaymentResponse(
            String paymentId,
            Long orderId,
            Long userId,
            BigDecimal amount,
            PaymentStatus status,
            LocalDateTime createdAt,
            LocalDateTime confirmedAt
    ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.confirmedAt = confirmedAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }
}
