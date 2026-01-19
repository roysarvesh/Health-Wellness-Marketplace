package com.wellness.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    // ----------------------------------
    // PRIMARY KEY
    // ----------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------------------------------
    // ORDER (PARENT)
    // ----------------------------------
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // ----------------------------------
    // PRODUCT (REFERENCE)
    // ----------------------------------
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // ----------------------------------
    // SNAPSHOT DATA
    // ----------------------------------
    @Column(nullable = false)
    private int quantity;

    // Snapshot price at order time (CRITICAL)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}
