package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class DisplayProductDto {
        private long productId;
        private String productName;
        private String productDescription;
        private String productImage;
        private boolean cloud;
        private float product1AttPrice;
        private String product1AttName;
}
