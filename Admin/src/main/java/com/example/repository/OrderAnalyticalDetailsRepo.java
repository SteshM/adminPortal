package com.example.repository;

import com.example.model.Analytics.OrderAnalyticalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderAnalyticalDetailsRepo  extends JpaRepository<OrderAnalyticalDetails,Long> {
    @Query(value = "EXEC get_order_analytics_details ?1", nativeQuery = true)
    public OrderAnalyticalDetails getAnalyticsDetails(Long orderId);
}
