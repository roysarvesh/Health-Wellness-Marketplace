package com.wellness.backend.controller;

import com.wellness.backend.dto.AdminPaymentResponse;
import com.wellness.backend.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    private final PaymentService service;

    public AdminPaymentController(PaymentService service) {
        this.service = service;
    }


    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(
            @RequestParam String paymentId
    ) {
        service.confirmPayment(paymentId);
        return ResponseEntity.ok("Payment confirmed successfully");
    }

 
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<String> refund(
            @PathVariable Long orderId
    ) {
        service.refund(orderId);
        return ResponseEntity.ok("Refund processed successfully");
    }


    @GetMapping
    public ResponseEntity<List<AdminPaymentResponse>> allPayments() {

        List<AdminPaymentResponse> response = service
                .allPayments()
                .stream()
                .map(p -> new AdminPaymentResponse(
                        p.getPaymentId(),
                        p.getOrder().getId(),
                        p.getOrder().getUser().getId(),
                        p.getAmount(),
                        p.getStatus(),
                        p.getCreatedAt(),
                        p.getConfirmedAt()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }
}
