package com.example.Admin.dto;

import com.example.Admin.enums.OrderStatus;
import com.example.Admin.model.Location;
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
