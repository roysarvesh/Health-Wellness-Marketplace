package com.wellness.backend.dto;

import com.wellness.backend.model.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private String paymentId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

    public PaymentResponse(String paymentId,
                           BigDecimal amount,
                           PaymentStatus status,
                           LocalDateTime createdAt,
                           LocalDateTime confirmedAt) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.confirmedAt = confirmedAt;
    }

    public String getPaymentId() { return paymentId; }
    public BigDecimal getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
}
