package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class DepotOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "quantityAttributeId")
    private QuantityAttribute quantityAttribute;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depotOrderId")
    private DepotOrder depotOrder;
    private int quantity;
}
