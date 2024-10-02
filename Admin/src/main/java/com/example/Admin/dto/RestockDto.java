package com.example.Admin.dto;

import lombok.Data;

@Data
public class RestockDto {
    private Long depotId;
    private Long productId;
    private Long qAttId;
    private int quantity;
}
