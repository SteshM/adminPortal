package com.example.dto;

import com.example.enums.OrderStatus;
import com.example.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RequestedOrderDto {
    private Long orderId;
    private String shopName;
    private String orderName;
    private String contact;
    private String retailerName;
    private Long depoId;
    private String depotName;
    private Location location;
    private OrderStatus status;
}
