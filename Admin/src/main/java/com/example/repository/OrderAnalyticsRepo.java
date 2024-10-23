package com.example.repository;

import com.example.model.Analytics.OrderAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderAnalyticsRepo extends JpaRepository<OrderAnalytics,Long> {
    public OrderAnalytics findByDateAndDepotId(int date, Long depotId);
    public List<OrderAnalytics> findByMonth(int month);
    public List<OrderAnalytics> findByYear(int year);
}
