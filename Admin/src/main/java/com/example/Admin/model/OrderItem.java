package com.example.Admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name="quantityAtId")
    private QuantityAttribute quantityAttribute;
    private int quantity;
    private float totalAmount;
    @ManyToOne
    @JoinColumn(name="orderId")
    private Order order;

    public static Long getProductId(OrderItem orderItem){
        return orderItem.getProduct().getProductId();
    }

}
