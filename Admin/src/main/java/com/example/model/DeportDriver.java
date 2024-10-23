package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class DeportDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "driverId")
    private MyUser driver;
    @ManyToOne
    @JoinColumn(name = "depotDepotId")
    private Depot depot;
}
