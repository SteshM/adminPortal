package com.example.Admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class RetailerDto {
        private Long retailerId;
        private String fullName;
        private String email;
        private String contact;
        private String createdOn;
}
