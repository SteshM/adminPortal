package com.example.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepotCartResponseDto {

        private Long cartItemId;
        private Long productId;
        private String productName;
        private Long qAttId;
        private String attrName;
        private int quantity;
}
