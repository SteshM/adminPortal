package com.example.Admin.repository;

import com.example.Admin.model.OrderItemDepot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDepotRepo extends JpaRepository<OrderItemDepot,Long> {
}
