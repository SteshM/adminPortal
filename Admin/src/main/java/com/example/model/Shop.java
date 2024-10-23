package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;
    private String shopName;
    private boolean softDelete;
    private String region;
    private String shopType;
    @ManyToOne
    @JoinColumn(name="userId")
    private MyUser myUser;
    @OneToOne
    private Location shopLocation;

}
