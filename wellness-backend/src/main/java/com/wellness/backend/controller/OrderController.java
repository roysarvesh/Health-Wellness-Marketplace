package com.wellness.backend.controller;

import com.wellness.backend.dto.OrderActionRequest;
import com.wellness.backend.dto.OrderResponse;
import com.wellness.backend.dto.OrderStatusResponse;
import com.wellness.backend.dto.PlaceOrderRequest;
import com.wellness.backend.model.Order;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // =============================
    // PLACE ORDER
    // =============================
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid PlaceOrderRequest request
    ) {
        return ResponseEntity.status(201)
                .body(service.placeOrder(user.getUser(), request));
    }

    // =============================
    // MY ORDERS (PAGINATED)
    // =============================
    @GetMapping("/my")
    public ResponseEntity<Page<Order>> myOrders(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                service.getMyOrders(user.getUser(), page, size)
        );
    }

    // =============================
    // GET SINGLE ORDER
    // =============================
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(
                service.getOrderResponse(user.getUser(), orderId)
        );
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponse> trackOrderStatus(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(
                service.trackOrderStatus(user.getUser(), orderId)
        );
    }

    // =============================
    // CANCEL ORDER
    // =============================
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderActionRequest request
    ) {
        return ResponseEntity.ok(
                service.cancelOrder(user.getUser(), orderId, request.getReason())
        );
    }

    // =============================
    // REQUEST RETURN
    // =============================
    @PutMapping("/{orderId}/return")
    public ResponseEntity<OrderResponse> requestReturn(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderActionRequest request
    ) {
        return ResponseEntity.ok(
                service.requestReturn(user.getUser(), orderId, request.getReason())
        );
    }

    // =============================
    // REQUEST REPLACEMENT
    // =============================
    @PutMapping("/{orderId}/replace")
    public ResponseEntity<OrderResponse> requestReplacement(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderActionRequest request
    ) {
        return ResponseEntity.ok(
                service.requestReplacement(user.getUser(), orderId, request.getReason())
        );
    }

    // =============================
    // REQUEST REFUND (ONLY AFTER RETURNED)
    // =============================
    @PutMapping("/{orderId}/refund")
    public ResponseEntity<OrderResponse> requestRefund(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderActionRequest request
    ) {
        return ResponseEntity.ok(
                service.refundOrder(user.getUser(), orderId, request.getReason())
        );
    }
}
