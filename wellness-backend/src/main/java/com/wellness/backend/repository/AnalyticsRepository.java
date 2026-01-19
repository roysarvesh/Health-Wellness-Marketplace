package com.wellness.backend.repository;

public interface AnalyticsRepository {

    long countTotalUsers();
    long countOrders();
    long countSessions();
}
