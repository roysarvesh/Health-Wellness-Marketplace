package com.wellness.backend.service;

import com.wellness.backend.model.Cart;
import com.wellness.backend.model.CartItem;
import com.wellness.backend.model.Product;
import com.wellness.backend.model.User;
import com.wellness.backend.repository.CartItemRepository;
import com.wellness.backend.repository.CartRepository;
import com.wellness.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final ProductRepository productRepo;

    public CartService(
            CartRepository cartRepo,
            CartItemRepository itemRepo,
            ProductRepository productRepo
    ) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.productRepo = productRepo;
    }

    // ------------------------------------------------
    // GET OR CREATE CART
    // ------------------------------------------------
    private Cart getOrCreateCart(User user) {
        return cartRepo.findByUser(user)
                .orElseGet(() -> cartRepo.save(new Cart(user)));
    }

    // ------------------------------------------------
    // ➕ ADD ITEM TO CART (MAX 20 PER CATEGORY)
    // ------------------------------------------------
    public Cart addItem(User user, Long productId, int qty) {

        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < qty) {
            throw new RuntimeException("Insufficient stock available");
        }

        Cart cart = getOrCreateCart(user);

        // 🔒 CATEGORY LIMIT CHECK (MAX 20)
        int categoryQty = cart.getItems().stream()
                .filter(i -> i.getProduct().getCategory() == product.getCategory())
                .mapToInt(CartItem::getQuantity)
                .sum();

        if (categoryQty + qty > 20) {
            throw new IllegalStateException(
                    "You can add a maximum of 20 items per category: "
                            + product.getCategory()
            );
        }

        CartItem item = itemRepo.findByCartAndProduct(cart, product)
                .orElse(null);

        if (item == null) {
            // 🆕 NEW ITEM → SET ONCE
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(qty);
            cart.getItems().add(item);
        } else {
            // 🔁 EXISTING ITEM → INCREMENT
            item.setQuantity(item.getQuantity() + qty);
        }

        return cartRepo.save(cart);
    }

    // ------------------------------------------------
    // 👀 VIEW CART
    // ------------------------------------------------
    public Cart viewCart(User user) {
        return getOrCreateCart(user);
    }

    // ------------------------------------------------
    // 🔄 UPDATE ITEM QUANTITY
    // ------------------------------------------------
    public Cart updateQuantity(User user, Long productId, int qty) {

        if (qty < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Cart cart = getOrCreateCart(user);

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = itemRepo.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (qty == 0) {
            // ❌ REMOVE ITEM
            cart.getItems().remove(item);
            itemRepo.delete(item);
        } else {

            // 🔒 CATEGORY LIMIT CHECK ON UPDATE
            int categoryQty = cart.getItems().stream()
                    .filter(i -> i.getProduct().getCategory() == product.getCategory())
                    .filter(i -> !i.getId().equals(item.getId()))
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            if (categoryQty + qty > 20) {
                throw new IllegalStateException(
                        "You can add a maximum of 20 items per category: "
                                + product.getCategory()
                );
            }

            item.setQuantity(qty);
        }

        return cartRepo.save(cart);
    }

    // ------------------------------------------------
    // ❌ REMOVE ITEM
    // ------------------------------------------------
    public Cart removeItem(User user, Long productId) {
        return updateQuantity(user, productId, 0);
    }

    // ------------------------------------------------
    // 🧹 CLEAR CART
    // ------------------------------------------------
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    // ------------------------------------------------
    // 🔢 CART ITEM COUNT (TOTAL QUANTITY)
    // ------------------------------------------------
    public int cartCount(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
