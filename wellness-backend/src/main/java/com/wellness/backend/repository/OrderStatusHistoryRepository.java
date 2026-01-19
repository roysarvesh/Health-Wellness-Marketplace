package com.wellness.backend.repository;

import com.wellness.backend.model.Order;
import com.wellness.backend.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository
        extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findByOrderOrderByChangedAtAsc(Order order);
}
