package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ShopImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean cloud;
    private String publicId;
    private String picture;
    @ManyToOne
    @JoinColumn(name = "shopId")
    private Shop shop;
}
