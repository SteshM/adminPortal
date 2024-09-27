package com.example.Admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "retailerId")
    private MyUser retailer;
    @ManyToOne
    @JoinColumn(name = "quantityAtId")
    private QuantityAttribute quantityAttribute;
    private int quantity;
}
