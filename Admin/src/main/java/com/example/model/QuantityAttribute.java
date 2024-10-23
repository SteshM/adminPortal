package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class QuantityAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quantityAttId;
    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;
    private String attrDescription;
    private float price;
    private float offerPrice;
    private String offerFrom;
    private String offerTo;
    private String picture;
    private boolean cloud;
    private String publicId;
}
