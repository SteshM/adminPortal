package com.example.repository;

import com.example.model.OrderItemDepot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDepotRepo extends JpaRepository<OrderItemDepot,Long> {
}
