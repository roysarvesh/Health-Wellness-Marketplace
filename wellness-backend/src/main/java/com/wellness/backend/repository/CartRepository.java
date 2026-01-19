package com.wellness.backend.repository;

import com.wellness.backend.model.Cart;
import com.wellness.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Each user has one cart
    Optional<Cart> findByUser(User user);
}
