package com.wellness.backend.analytics;

import com.wellness.backend.model.OrderStatus;
import com.wellness.backend.model.PaymentStatus;
import com.wellness.backend.repository.OrderRepository;
import com.wellness.backend.repository.PaymentRepository;
import com.wellness.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AnalyticsService {

    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public AnalyticsService(
            UserRepository userRepo,
            OrderRepository orderRepo,
            PaymentRepository paymentRepo
    ) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    public Map<String, Object> getDashboardStats() {

        long totalUsers = userRepo.count();
        long totalOrders = orderRepo.count();

        long successfulPayments =
                paymentRepo.countByStatus(PaymentStatus.SUCCESS);

        long refundedPayments =
                paymentRepo.countByStatus(PaymentStatus.REFUNDED);

        BigDecimal totalRevenue =
                paymentRepo.sumAmountByStatus(PaymentStatus.SUCCESS);

        BigDecimal refundedAmount =
                paymentRepo.sumAmountByStatus(PaymentStatus.REFUNDED);

        long refundRequests =
                orderRepo.countByStatus(OrderStatus.REFUND_REQUESTED);

        return Map.of(
                "totalUsers", totalUsers,
                "totalOrders", totalOrders,
                "successfulPayments", successfulPayments,
                "refundedPayments", refundedPayments,
                "totalRevenue", totalRevenue,
                "refundedAmount", refundedAmount,
                "refundRequests", refundRequests
        );
    }
}
