package com.example.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long cartItemId;
    private Long productId;
    private Long retailerId;
    private Long quantityAtId;
    private int quantity;
}
