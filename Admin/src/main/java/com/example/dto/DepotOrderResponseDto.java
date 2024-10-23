package com.example.dto;

import com.example.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity
    public class DepotOrderResponseDto {
        @Id
        private Long depotOrderId;
        private String orderName;
        private String depotTo;
        private String depotFrom;
        private Long driverId;
        private String driverName;
        private String truckNo;
        private String orderDate;
        private OrderStatus orderStatus;
}
