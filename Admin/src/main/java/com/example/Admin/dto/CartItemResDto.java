package com.example.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CartItemResDto {
    private Long cartItemId;
    private long productId;
    private String productName;
    private String productAttrName;
    private long productAttrId;
    private float productAttrPrice;
    private boolean cloud;
    private String picture;
    private int quantity;
}
