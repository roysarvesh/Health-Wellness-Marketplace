package com.wellness.backend.service;

import com.wellness.backend.model.Order;
import com.wellness.backend.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderStatusValidator {

    public void validate(Order order, Set<OrderStatus> allowedStatuses, String action) {
        if (!allowedStatuses.contains(order.getStatus())) {
            throw new IllegalStateException(
                    "Cannot " + action + " order with status: " + order.getStatus()
            );
        }
    }
}
