package com.wellness.backend.dto;
import com.wellness.backend.model.OrderStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusResponse {

    private Long orderId;
    private OrderStatus status;
    private LocalDateTime lastUpdated;
}
