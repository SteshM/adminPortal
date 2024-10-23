package com.example.repository;

import com.example.model.DepotOrder;
import com.example.model.DepotOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotOrderItemRepo extends JpaRepository<DepotOrderItem,Long> {
    public List<DepotOrderItem> findByDepotOrder(DepotOrder depotOrder);

}
