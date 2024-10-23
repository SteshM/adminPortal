package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class OrderItemDetails {
        private long productId;
        private String productName;
        private String productDescription;
        private float totalPrice;
        private float quantity;
        private String productImage;
        private long productQatid;
        private String productQattName;
        private boolean cloud;
    }
