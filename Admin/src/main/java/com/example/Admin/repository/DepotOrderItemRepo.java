package com.example.Admin.repository;

import com.example.Admin.model.DepotOrder;
import com.example.Admin.model.DepotOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotOrderItemRepo extends JpaRepository<DepotOrderItem,Long> {
    public List<DepotOrderItem> findByDepotOrder(DepotOrder depotOrder);

}
