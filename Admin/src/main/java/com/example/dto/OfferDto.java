package com.example.dto;

import lombok.Data;
    @Data
    public class OfferDto {
        private Long productId;
        private Long productAttrId;
        private String offerFrom;
        private String offerTo;
        private float offerPrice;
}
