package com.example.Admin.dto;


import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long shopId;
    private List<OrderItemDto> orderItems;
}
