package com.wellness.backend.controller;

import com.wellness.backend.dto.PaymentResponse;
import com.wellness.backend.model.Payment;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiate(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam Long orderId
    ) {
        Payment payment = service.initiatePayment(user.getUser(), orderId);

        return ResponseEntity.ok(
                new PaymentResponse(
                        payment.getPaymentId(),
                        payment.getAmount(),
                        payment.getStatus(),
                        payment.getCreatedAt(),
                        payment.getConfirmedAt()
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> history(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<PaymentResponse> response = service
                .paymentHistory(user.getUser().getId())
                .stream()
                .map(p -> new PaymentResponse(
                        p.getPaymentId(),
                        p.getAmount(),
                        p.getStatus(),
                        p.getCreatedAt(),
                        p.getConfirmedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
