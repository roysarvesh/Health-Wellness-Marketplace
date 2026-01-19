package com.wellness.backend.service;

import com.wellness.backend.dto.*;
import com.wellness.backend.model.*;
import com.wellness.backend.repository.CartRepository;
import com.wellness.backend.repository.OrderRepository;
import com.wellness.backend.repository.OrderStatusHistoryRepository;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    public OrderService(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            OrderStatusHistoryRepository statusHistoryRepository,
            EmailService emailService,
            SmsService smsService
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public OrderResponse placeOrder(User user, PlaceOrderRequest request) {

        Order order = createOrderLogic(user, request);
        Order savedOrder = orderRepository.save(order);

        recordStatus(savedOrder, OrderStatus.PLACED);

        emailService.sendOrderStatusEmail(user.getEmail(), savedOrder);
        smsService.sendSms(savedOrder.getContactNumber(), "Order placed successfully");

        return mapToResponse(savedOrder);
    }

    private Order createOrderLogic(User user, PlaceOrderRequest req) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot place order with empty cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(req.getCustomerName());
        order.setAddressLine(req.getAddressLine());
        order.setCity(req.getCity());
        order.setState(req.getState());
        order.setCountry(req.getCountry());
        order.setPincode(req.getPincode());
        order.setContactNumber(req.getContactNumber());
        order.setStatus(OrderStatus.PLACED);

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());
            order.addItem(oi);
        }

        cart.getItems().clear();
        return order;
    }


    private void recordStatus(Order order, OrderStatus status) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        statusHistoryRepository.save(history);
    }


    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .expectedDeliveryDate(order.getExpectedDeliveryDate())
                .build();
    }


    public Page<Order> getMyOrders(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    public OrderResponse getOrderResponse(User user, Long orderId) {
        return mapToResponse(getUserOrder(user, orderId));
    }

    private Order getUserOrder(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Unauthorized access");
        }
        return order;
    }

    private void validateStatus(Order order, Set<OrderStatus> allowed, String action) {
        if (!allowed.contains(order.getStatus())) {
            throw new IllegalStateException(
                    "Cannot " + action + " order with status " + order.getStatus()
            );
        }
    }

    // ==================================================
    // USER ACTIONS
    // ==================================================
    public OrderResponse cancelOrder(User user, Long id, String reason) {
        Order order = getUserOrder(user, id);
        validateStatus(order, Set.of(OrderStatus.PLACED), "cancel");

        order.setStatus(OrderStatus.CANCELLED);
        order.setLastAction("CANCEL");
        order.setActionReason(reason);

        Order saved = orderRepository.save(order);
        recordStatus(saved, OrderStatus.CANCELLED);

        return mapToResponse(saved);
    }

    public OrderResponse requestReturn(User user, Long id, String reason) {
        Order order = getUserOrder(user, id);
        validateStatus(order, Set.of(OrderStatus.DELIVERED), "request return");

        order.setStatus(OrderStatus.RETURN_REQUESTED);
        order.setLastAction("RETURN_REQUEST");
        order.setActionReason(reason);

        Order saved = orderRepository.save(order);
        recordStatus(saved, OrderStatus.RETURN_REQUESTED);

        return mapToResponse(saved);
    }

    public OrderResponse requestReplacement(User user, Long id, String reason) {
        Order order = getUserOrder(user, id);
        validateStatus(order, Set.of(OrderStatus.DELIVERED), "request replacement");

        order.setStatus(OrderStatus.REPLACEMENT_REQUESTED);
        order.setLastAction("REPLACEMENT_REQUEST");
        order.setActionReason(reason);

        Order saved = orderRepository.save(order);
        recordStatus(saved, OrderStatus.REPLACEMENT_REQUESTED);

        return mapToResponse(saved);
    }

    public OrderResponse refundOrder(User user, Long id, String reason) {
        Order order = getUserOrder(user, id);
        validateStatus(order, Set.of(OrderStatus.RETURNED), "request refund");

        order.setStatus(OrderStatus.REFUND_REQUESTED);
        order.setLastAction("REFUND_REQUEST");
        order.setActionReason(reason);

        Order saved = orderRepository.save(order);
        recordStatus(saved, OrderStatus.REFUND_REQUESTED);

        return mapToResponse(saved);
    }

    // ==================================================
    // ADMIN
    // ==================================================
    public Page<Order> getAllOrders(int page, int size) {
        return orderRepository.findAll(
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }

    public Page<Order> getOrderByStatus(OrderStatus status, int page, int size) {
        return orderRepository.findByStatus(
                status,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }

    public Order markStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order saved = orderRepository.save(order);
        recordStatus(saved, status);

        return saved;
    }

    public Order approveRefund(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.REFUND_REQUESTED) {
            throw new IllegalStateException("Refund not requested");
        }

        order.setStatus(OrderStatus.REFUNDED);
        order.setRefundedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        recordStatus(saved, OrderStatus.REFUNDED);

        return saved;
    }

    // ==================================================
    // TRACK ORDER
    // ==================================================
    public OrderStatusResponse trackOrderStatus(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())
                && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }

        return OrderStatusResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .lastUpdated(order.getUpdatedAt())
                .build();
    }
}
