package com.wellness.backend.repository;

import com.wellness.backend.model.Cart;
import com.wellness.backend.model.CartItem;
import com.wellness.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Check if product already exists in cart
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Get all items of a cart
    List<CartItem> findByCart(Cart cart);
}
