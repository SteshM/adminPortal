package com.example.dto;

import com.example.enums.OrderStatus;
import com.example.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedAssignedOrderDto {
       private Long orderId;
        private String shopName;
        private String orderName;
        private String contact;
        private String retailerName;
        private Long depoId;
        private String depotName;
        private Location location;
        private OrderStatus status;
        private Long driverId;
        private String driverName;
        private String truckNo;
        private String driverContact;
        private String driverEmail;
}
