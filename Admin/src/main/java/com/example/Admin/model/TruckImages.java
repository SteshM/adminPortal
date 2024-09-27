package com.example.Admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TruckImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean cloud;
    private String publicId;
    private String picture;
    @ManyToOne
    @JoinColumn(name = "truckId")
    private Truck truck;
}