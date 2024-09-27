package com.example.Admin.repository;

import com.example.Admin.enums.OrderStatus;
import com.example.Admin.model.Depot;
import com.example.Admin.model.DepotOrder;
import com.example.Admin.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotOrderRepo extends JpaRepository<DepotOrder,Long> {
    public List<DepotOrder> findByDepot(Depot depot);
    public List<DepotOrder> findByDepotAndStatus(Depot depot, OrderStatus orderStatus);
    public List<DepotOrder> findByAssignedDepot(Depot assignedDepot);
    public List<DepotOrder> findByMyUser(MyUser driver);
    public List<DepotOrder> findByStatus(OrderStatus orderStatus);
}
