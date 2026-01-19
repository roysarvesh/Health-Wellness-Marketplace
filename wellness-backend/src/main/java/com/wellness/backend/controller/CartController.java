package com.wellness.backend.controller;

import com.wellness.backend.dto.AddToCartRequest;
import com.wellness.backend.dto.UpdateCartRequest;
import com.wellness.backend.model.Cart;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add item
    @PostMapping("/add")
    public ResponseEntity<Cart> add(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AddToCartRequest req
    ) {
        return ResponseEntity.ok(
                cartService.addItem(
                        userDetails.getUser(),
                        req.getProductId(),
                        req.getQuantity()
                )
        );
    }

    // View cart
    @GetMapping
    public ResponseEntity<Cart> view(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(cartService.viewCart(userDetails.getUser()));
    }

    // Update quantity
    @PutMapping("/update")
    public ResponseEntity<Cart> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateCartRequest req
    ) {
        return ResponseEntity.ok(
                cartService.updateQuantity(
                        userDetails.getUser(),
                        req.getProductId(),
                        req.getQuantity()
                )
        );
    }

    // Remove item
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> remove(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                cartService.removeItem(userDetails.getUser(), productId)
        );
    }

    // Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<String> clear(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.clearCart(userDetails.getUser());
        return ResponseEntity.ok("Cart cleared");
    }

    // Cart count
    @GetMapping("/count")
    public ResponseEntity<Integer> count(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(cartService.cartCount(userDetails.getUser()));
    }
}
