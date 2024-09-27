package com.example.Admin.repository;

import com.example.Admin.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Inventory findByProductId(Long productId);

    Inventory findByDepotId(Long depotId);
}
