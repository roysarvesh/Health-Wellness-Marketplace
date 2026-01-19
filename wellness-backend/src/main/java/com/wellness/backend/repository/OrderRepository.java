package com.wellness.backend.repository;

import com.wellness.backend.model.Order;
import com.wellness.backend.model.OrderStatus;
import com.wellness.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
    Page<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    Optional<Order> findByIdAndUser(Long id, User user);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByStatus(OrderStatus status);
}