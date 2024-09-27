package com.example.Admin.model;

import com.example.Admin.service.Components.utils.DateUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long truckId;
    private String truckNo;
    private String make;
    private String model;
    private int year;
    private String color;
    private double loadCapacity;
    private double fuelCapacity;
    private String fuelType;
    private boolean isAvailable;
    private boolean isUnderMaintenance;
    @ManyToOne
    @JoinColumn(name="userId")
    private MyUser myUser;
    private String createdOn = DateUtils.dateNowString();

}
