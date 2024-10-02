package com.example.Admin.dto;

import lombok.Data;

@Data
public class TruckUpdateDto {
    private Long truckId;
    private String make;
    private String model;
    private int year;
    private String color;
    private double loadCapacity;
    private double fuelCapacity;
    private String fuelType;
}
