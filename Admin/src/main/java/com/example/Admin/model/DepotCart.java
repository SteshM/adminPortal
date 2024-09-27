package com.example.Admin.model;

import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class DepotCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    @ManyToOne
    @JoinColumn(name = "adminId")
    private MyUser admin;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "quantityAttId")
    private QuantityAttribute quantityAttribute;
    private int quantity;
    private String createdOn = DateUtils.dateNowString();
}
