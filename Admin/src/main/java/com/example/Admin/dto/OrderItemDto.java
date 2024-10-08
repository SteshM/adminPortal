package com.example.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private int quantity;
    private Long qAttrId;
    private Long cartItemId;
}
