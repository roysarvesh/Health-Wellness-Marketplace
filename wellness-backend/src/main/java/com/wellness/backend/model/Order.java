package com.wellness.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    // ----------------------------------
    // PRIMARY KEY
    // ----------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------------------------------
    // USER (ORDER OWNER)
    // ----------------------------------
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ----------------------------------
    // CUSTOMER SNAPSHOT (IMMUTABLE)
    // ----------------------------------
    @Column(nullable = false, length = 150)
    private String customerName;

    // ----------------------------------
    // ORDER ITEMS
    // ----------------------------------
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    // ----------------------------------
    // DELIVERY DETAILS
    // ----------------------------------
    @Column(nullable = false, length = 255)
    private String addressLine;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 6)
    private String pincode;

    @Column(nullable = false, length = 10)
    private String contactNumber;

    // ----------------------------------
    // ORDER ACTION TRACKING (NEW)
    // ----------------------------------
    @Column(length = 500)
    private String actionReason;

    @Column(length = 30)
    private String lastAction;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    // ----------------------------------
    // PAYMENT & STATUS
    // ----------------------------------
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    // ----------------------------------
    // TIMESTAMPS
    // ----------------------------------
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime expectedDeliveryDate;

    // ----------------------------------
    // JPA CALLBACKS
    // ----------------------------------
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        // Default expected delivery: +5 days
        this.expectedDeliveryDate = now.plusDays(5);

        if (this.totalAmount == null) {
            this.totalAmount = BigDecimal.ZERO;
        }

        if (this.status == null) {
            this.status = OrderStatus.PLACED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ----------------------------------
    // HELPER METHODS
    // ----------------------------------
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);

        this.totalAmount = this.totalAmount.add(
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);

        this.totalAmount = this.totalAmount.subtract(
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
