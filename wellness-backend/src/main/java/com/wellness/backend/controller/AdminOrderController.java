package com.wellness.backend.controller;

import com.wellness.backend.model.OrderStatus;
import com.wellness.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService service;

    public AdminOrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                service.getAllOrders(page, size)
        );
    }


    @GetMapping("/requests")
    public ResponseEntity<?> getOrdersByStatus(
            @RequestParam OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                service.getOrderByStatus(status, page, size)
        );
    }


    @PutMapping("/{id}/ship")
    public ResponseEntity<?> shipOrder(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.markStatus(id, OrderStatus.SHIPPED)
        );
    }

    @PutMapping("/{id}/out-for-delivery")
    public ResponseEntity<?> outForDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.markStatus(id, OrderStatus.OUT_FOR_DELIVERY)
        );
    }


    @PutMapping("/{id}/deliver")
    public ResponseEntity<?> deliverOrder(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.markStatus(id, OrderStatus.DELIVERED)
        );
    }


    @PutMapping("/{id}/approve-return")
    public ResponseEntity<?> approveReturn(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.markStatus(id, OrderStatus.RETURNED)
        );
    }


    @PutMapping("/{id}/approve-refund")
    public ResponseEntity<?> approveRefund(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.approveRefund(id)
        );
    }
}
