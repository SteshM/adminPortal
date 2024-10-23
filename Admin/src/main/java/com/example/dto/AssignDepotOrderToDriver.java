package com.example.dto;

import lombok.Data;

@Data
public class AssignDepotOrderToDriver {
    private Long depotId;
    private Long depotOrderId;
    private Long driverId;
}
