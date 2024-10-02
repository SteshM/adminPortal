package com.example.Admin.dto;

import lombok.Data;

@Data
public class DriverUpdateDto {
    private Long driverId;
    private String fullName;
    private Long truckId;
}
