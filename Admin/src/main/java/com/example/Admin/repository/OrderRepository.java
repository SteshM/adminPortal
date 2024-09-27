package com.example.Admin.repository;

import com.example.Admin.enums.OrderStatus;
import com.example.Admin.model.Depot;
import com.example.Admin.model.MyUser;
import com.example.Admin.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByDeliveryCode(String code);
    List<Order> findByDriver(MyUser driver);
    List<Order> findByRetailer(MyUser retailer);
    List<Order> findByDepot(Depot depot);
    List<Order> findByStatus(OrderStatus received);
    List<Order> findByDepotAndStatus(Depot depot, OrderStatus dispatched);
}
