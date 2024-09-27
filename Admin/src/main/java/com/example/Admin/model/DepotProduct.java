package com.example.Admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepotProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="depotId")
    private Depot depot;
    @ManyToOne
    @JoinColumn(name= "productId")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "quantityAttId")
    private QuantityAttribute quantityAt;
    private int quantity;
    //private String coverPic;


}
