package com.example.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class DriverDto {
        @Id
        @GeneratedValue
        private Long driverId;
        private String fullName;
        private String email;
        private String employeeNo;
        private String truckNo;
        private String contact;
        private String depotName;
        private Long depotId;
        private String createdOn;
}
