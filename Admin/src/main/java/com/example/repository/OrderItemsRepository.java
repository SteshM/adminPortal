package com.example.repository;

import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.QuantityAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItem,Long> {
    public List<OrderItem> findByOrder(Order order);
    public List<OrderItem> findByOrderAndQuantityAttribute(Order order, QuantityAttribute quantityAttribute);
    public Integer countByOrder(Order order);
}
