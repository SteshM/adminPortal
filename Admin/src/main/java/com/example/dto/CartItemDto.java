package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private Long cartItemId;
    private long productId;
    private String productName;
    private String productAttrName;
    private long productAttrId;
    private float productAttrPrice;
    private boolean cloud;
    private String picture;
    private int quantity;
    private float offerPrice;
    private String offerFrom;
    private String offerTo;
}
