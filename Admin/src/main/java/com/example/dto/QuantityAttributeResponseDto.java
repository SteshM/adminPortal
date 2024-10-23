package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuantityAttributeResponseDto {
    private Long quantityAttId;
    private Long productId;
    private String attrDescription;
    private float price;
    private float beforeOffer;
    private String offerTo;
    private boolean offerOn;
    private String picture;
    private boolean cloud;
    private String publicId;
}
